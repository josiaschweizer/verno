# Verno ? Security Review

**Scope:** full static review of `/Users/josiaschweizer/dev/verno` ? backend Java (gateway, server/RPC, common, db), React/TypeScript frontend, config, deployment scripts, dependencies.
**Method:** read-only source audit. No code was changed, nothing was committed, and no live attack was run against running services (they were not running, and per your instruction). Findings are written as attack scenarios with the exact file/line to fix.
**Date:** 2026-07-09

---

## Summary

| # | Severity | Finding |
|---|----------|---------|
| 1 | **Critical** | Gateway API Basic-Auth credentials are shipped inside the public React bundle (`VITE_PROVISIONER_API_PASS`) |
| 2 | **Critical** | Server `/rpc` endpoint enforces no authentication and trusts the raw `X-Mandant` header for tenant selection |
| 3 | **High** | Cloud Run tenant provisioning deploys `--allow-unauthenticated` by default with no ingress restriction |
| 4 | **High** | Committed GCP DB credentials in `application-dev-gcp.properties` |
| 5 | **High** | Outdated dependencies with known CVEs (iText 2.1.7, JasperReports 6.20.6, OGNL) |
| 6 | Medium | Production CORS allows `http://localhost:5173` with credentials |
| 7 | Medium | Access token passed as URL query parameter (`access_token`) |
| 8 | Medium | Over-verbose logging in prod-adjacent config (`security=TRACE`, `show-sql=true`) |
| 9 | Low | Weak default/dev DB credentials (`verno`/`verno`) in tracked files |
| 10 | Low | JWT secret length not validated; non-UTF8 `getBytes()` in one codec |

**What is done well (no action needed):** Stripe webhook signature verification is correct; file storage has solid path-traversal and symlink protection; temp filenames are sanitized; stored access tokens are SHA-256 hashed; all SQL is parameterized (no injection found); sessions are stateless; prod cookies are `Secure` + `HttpOnly` + `SameSite`.

---

## 1. Critical ? API credentials embedded in the public frontend bundle

**Where:** `typescript/apps/onboarding/src/lib/api/{loginApi,tenantsApi,emailApi}.ts`

```ts
const apiUser = env.VITE_PROVISIONER_API_USER || 'verno'
const apiPass = env.VITE_PROVISIONER_API_PASS || 'verno'
const client = createApiClient({ baseUrl, basicAuth: { user: apiUser, pass: apiPass } })
```

`VITE_*` variables are inlined into the JavaScript bundle at build time and served to every visitor ? they are **not** secret. The onboarding app uses these credentials to call privileged gateway endpoints:

- `POST /api/v1/tenants` (create tenant), `DELETE /api/v1/tenants/{key}` (delete tenant)
- `POST /api/v1/login`, `POST /api/v1/email`, tenant counts, member counts

These endpoints are protected by the gateway's single Basic-Auth user (`GatewaySecurityConfig`), which is exactly the credential being handed to the browser. **Anyone who loads the onboarding site can open dev-tools, read the credential from the bundle, and then create/delete tenants and send mail directly against `/api/v1/**`.** The fallback default is `verno`/`verno`.

**Attack:** open onboarding site ? view source/network ? extract Basic-Auth header ? `curl -u verno:verno https://gateway/api/v1/tenants` ? full tenant administration.

**Fix:** these calls must not be made directly from the browser with shared admin credentials. Move provisioning/admin calls behind a server-side backend-for-frontend that holds the credential, and give the browser a per-user, scoped, expiring session instead. At minimum, never place any real credential in a `VITE_` variable, and split the privileged provisioner API onto a separately-authenticated path.

---

## 2. Critical ? Server `/rpc` has no auth enforcement and trusts the `X-Mandant` header

**Where:**
- `server/.../config/security/ServerSecurityConfig.java` ? `/rpc` chain is `authorizeHttpRequests(auth -> auth.anyRequest().permitAll())`
- `server/.../config/security/InternalRpcAuthFilter.java` ? only sets authentication *if* a `Bearer` token is present; a request with **no** token simply passes through
- `server/.../config/tenant/TenantConfig.java` ? registers `TenantFilter` on `/rpc` at `HIGHEST_PRECEDENCE + 10` (runs **before** Spring Security)
- `server/.../config/tenant/TenantResolver.java` ? reads `X-Mandant` and uses it directly as the numeric tenant id

The internal RPC token is meant to authenticate gateway?server calls, but the server never *requires* it. Because `TenantFilter` runs first and sets the tenant straight from the `X-Mandant` header, an attacker who can reach the server can select any tenant with no credential:

```
POST /rpc  HTTP/1.1
X-Mandant: 42
Content-Type: application/json

{ "resource": "...", "method": "...", "args": [...] }
```

The scoped-repository aspect (`TenantFilterEnabler.getRequired()`) does fail closed when *no* tenant is set ? but here the attacker supplies one via the header, so scoped queries execute against the chosen tenant. `@UnscopedQuery` bootstrap lookups (e.g. by token hash, by Stripe customer id) are reachable regardless. The entire model relies on the server never being reachable by an attacker ? see #3.

**Fix:** make the `/rpc` chain `authenticated()` and reject any request whose internal RPC token is missing/invalid (don't treat "no token" as anonymous-allowed). Derive the tenant **only** from the verified token claim, not from a client-supplied `X-Mandant` header on the server tier. Remove or restrict the `TenantFilter`/`X-Mandant` path so headers can never override the authenticated tenant.

---

## 3. High ? Cloud Run services deployed publicly, no ingress restriction

**Where:** `scripts/provision-tenant.sh`

```
--allow-unauthenticated <true|false> (default: true)
...
gcloud run deploy "${SERVICE}" ... ${ALLOW_FLAG}
```

The deploy defaults to `--allow-unauthenticated=true` and sets no `--ingress internal` and no VPC connector. Combined with #2, if the server/RPC tier is deployed this way it is reachable from the internet with no authentication. Even the gateway/UI should front the server, never expose it.

**Fix:** deploy the server/RPC service with `--ingress=internal` (or internal-and-cloud-load-balancing) and `--no-allow-unauthenticated`, reachable only from the gateway via a VPC connector or Cloud Run service-to-service IAM auth. Default `ALLOW_UNAUTH` to `false` in the script.

---

## 4. High ? Committed GCP database credentials

**Where:** `ui/src/main/resources/application-dev-gcp.properties` (tracked in git)

```
spring.datasource.username=verno_app
spring.datasource.password=VernoTest12345
```

A real username/password for a GCP-reachable database (via the Cloud SQL proxy on `127.0.0.1:5433`) is committed to the repository. Anyone with repo access ? or anyone who later obtains the git history ? has this credential.

**Fix:** remove the password from the file, source it from an env var / Secret Manager, rotate the DB password, and purge it from git history (`git filter-repo` / BFG). Confirm this credential is dev-only and not reused elsewhere.

---

## 5. High ? Outdated dependencies with known vulnerabilities

**Where:** root `pom.xml`

- `itext.version = 2.1.7` ? iText 2.1.7 (2009, last MPL release) has long-known XXE and other parser vulnerabilities; unmaintained.
- `jasperreports.version = 6.20.6` ? JasperReports has multiple published RCE/SSRF/expression-injection CVEs in this range.
- `ognl.version = 3.3.4` ? OGNL expression evaluation is a recurring RCE vector (esp. when combined with JasperReports templates).

**Fix:** run an SCA scan (`mvn org.owasp:dependency-check-maven:check` and `pnpm audit`) and treat it as recurring CI. Upgrade JasperReports to a patched line, replace iText 2.1.7 (you already use openhtmltopdf ? consolidate on it), and ensure report templates are never built from untrusted input while OGNL is on the classpath.

---

## 6. Medium ? Production CORS allows localhost with credentials

**Where:** `gateway/.../security/ApiCorsConfig.java`

```java
config.setAllowCredentials(true);
config.setAllowedOrigins(List.of(
  "https://www.verno-app.ch", "https://verno-app.ch",
  "https://payment.verno-app.ch", "http://localhost:5173"));
config.setAllowedHeaders(List.of("*"));
```

Shipping `http://localhost:5173` as an allowed credentialed origin in production lets a local attacker page make authenticated cross-origin calls. `TODO CONSTANTS!!!` is still in the file.

**Fix:** drop `localhost` from the production origin list (make the list profile-specific), and prefer an explicit allowed-headers list over `*`.

---

## 7. Medium ? Access token in URL query parameter

**Where:** `gateway/.../security/ResourceAccessFilter.java`, `ApiQueryParam.ACCESS_TOKEN = "access_token"`

The resource access token is read from `?access_token=...`. URLs land in server/proxy/CDN access logs, browser history, and `Referer` headers, so the token leaks more easily than a header would. TTL is short (5 min), which limits impact.

**Fix:** accept the token via an `Authorization`/custom header instead of a query parameter where feasible.

---

## 8. Medium ? Over-verbose logging

**Where:** `ui/src/main/resources/application.properties` (`org.springframework.security=TRACE`), `server/.../application.properties` (`spring.jpa.show-sql=true`, `logging.level.ch.verno=DEBUG`)

Security TRACE and SQL logging can emit tokens, credentials, and personal data into logs. `application.properties` sets `spring.profiles.active=dev`, so confirm prod truly overrides these.

**Fix:** keep TRACE/DEBUG/`show-sql` out of any profile that can run in production; default to `INFO`.

---

## 9. Low ? Weak dev/default credentials in tracked files

`server/.../application.properties` and `docker-compose.yml` use `verno`/`verno` for Postgres; the gateway API user falls back to `verno`/`verno`. Dev-only, but the same weak pattern shows up in the frontend default (#1). Ensure no environment ever runs on these defaults.

## 10. Low ? JWT secret handling

`InternalRpcTokenCodec` / `ResourceAccessTokenCodec` build the HMAC key directly from the secret string. jjwt rejects keys shorter than 256 bits at runtime (not build time), so a short secret fails only when first exercised. `ResourceAccessTokenCodec` uses `secret.getBytes()` (platform default charset) while the internal codec uses UTF-8 ? align both to UTF-8 and validate secret length at startup.

---

## Recommended order of remediation

1. Rotate and remove the frontend-exposed API credential (#1) and the committed DB password (#4) ? these are live secret exposures.
2. Lock down the server tier: require the internal token, derive tenant from the token only, and set Cloud Run ingress to internal (#2, #3).
3. Fix prod CORS (#6) and logging (#8).
4. Stand up SCA scanning and upgrade the vulnerable libraries (#5).
5. Clean up the remaining Low items (#7, #9, #10).