package ch.verno.common.lib.api;

import org.jetbrains.annotations.NonNls;

@SuppressWarnings("HardcodedFileSeparator")
public class ApiUrl {

  @NonNls private static final String API = "/api";
  @NonNls private static final String INTERNAL = "/internal";
  @NonNls private static final String PUBLIC = "/public";
  @NonNls private static final String AUTH = "/auth";
  @NonNls private static final String V1 = "/v1";
  public static final String VERSION = V1;

  /**
   * The default base api is a normal api which needs authentication but is used from outside (e.g. rest api calls or create tenant requests)
   */
  public static final String BASE_API = API + VERSION;
  /**
   * The internal api is a server-to-server only api which should not be able to call from outside
   */
  public static final String INTERNAL_BASE_API = INTERNAL + BASE_API;
  /**
   * The public api is a 100% public api which doesn't require any authentication
   * public / api / version
   */
  public static final String PUBLIC_BASE_API = PUBLIC + BASE_API;
  /**
   * The public auth api is a public api route which requires a scoped token
   * public / api / version / auth
   */
  public static final String PUBLIC_AUTH_BASE_API = PUBLIC_BASE_API + AUTH;

  // internal end points
  @NonNls public static final String TEMP_FILE_IMPORT = BASE_API + "/temp-file/import";
  @NonNls public static final String TEMP_FILE_EXPORT = BASE_API + "/temp-file/export";
  @NonNls public static final String FILES = BASE_API + "/files";

  // normal end points
  @NonNls public static final String TENANTS = BASE_API + "/tenants";
  @NonNls public static final String APPLICATION = BASE_API + "/application";
  @NonNls public static final String EMAIL = BASE_API + "/email";

  @NonNls public static final String BILLING = BASE_API + "/billing";

  // public auth endpoints
  @NonNls public static final String TEMP_FILE_REPORT_PUBLIC_AUTH = PUBLIC_AUTH_BASE_API + "/temp-file/report";

  // public endpoints
  @NonNls public static final String HEALTH = PUBLIC_BASE_API + "/health";
  @NonNls public static final String PUBLIC_BILLING = PUBLIC_BASE_API + "/billing";
  @NonNls public static final String PUBLIC_BILLING_ACCESS_TOKEN = PUBLIC_BILLING + "/access-token";
  @NonNls public static final String PUBLIC_BILLING_WEBHOOK = PUBLIC_BILLING + "/webhook";
  @NonNls public static final String PUBLIC_BILLING_SESSION = PUBLIC_BILLING + "/session";

  // ending-endpoints
  @NonNls public static final String COUNT = "/count";

  @NonNls public static final String RESOLVE_ACCESS_TOKEN = "/resolve";
  @NonNls public static final String START_STRIPE_SESSION = "/start";
  @NonNls public static final String STRIP_WEBHOOK = "/stripe";


  // additional constants
  @NonNls public static final String DISPOSITION_ATTACHMENT = "?disposition=attachment";
  @NonNls public static final String DISPOSITION_INLINE = "?disposition=inline";

  @NonNls public static final String ENTRY_TOKEN = "entry?token=";

}
