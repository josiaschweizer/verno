#!/usr/bin/env bash
set -e

# -------------------------------------------------
# Postgres Passwort für postgres-User setzen
# -------------------------------------------------

POSTGRES_PASS="$(openssl rand -base64 32 | tr -d '\n')"
echo "POSTGRES_PASS=${POSTGRES_PASS}"

gcloud sql users set-password postgres \
  --instance=verno-sql-ch \
  --password="$POSTGRES_PASS"

# -------------------------------------------------
# HINWEIS:
# In einem zweiten Terminal starten:
#
# cloud-sql-proxy keen-jigsaw-482516-e6:europe-west6:verno-sql-ch \
#   --address 127.0.0.1 \
#   --port 5433
# -------------------------------------------------

# -------------------------------------------------
# Control-DB Setup (psql via Proxy)
# -------------------------------------------------

PGPASSWORD="$POSTGRES_PASS" \
psql -h 127.0.0.1 -p 5433 -U postgres -d verno_control <<'SQL'

create extension if not exists pgcrypto;

create table if not exists tenants (
  id uuid primary key default gen_random_uuid(),
  tenant_key text not null unique,
  tenant_name text not null,
  tenant_subdomain text not null unique,
  preferred_language text not null default 'de',
  status text not null default 'active',
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create index if not exists tenants_subdomain_idx on tenants(tenant_subdomain);
create index if not exists tenants_status_idx on tenants(status);

insert into tenants (tenant_key, tenant_name, tenant_subdomain, preferred_language)
values ('demo', 'Demo Tenant', 'demo', 'de')
on conflict (tenant_key) do nothing;

grant connect on database verno_control to verno_app;

create schema if not exists tenant_demo authorization postgres;

grant usage on schema tenant_demo to verno_app;
grant create on schema tenant_demo to verno_app;

alter default privileges in schema tenant_demo
grant select, insert, update, delete on tables to verno_app;

alter default privileges in schema tenant_demo
grant usage, select, update on sequences to verno_app;

alter default privileges in schema tenant_demo
grant execute on functions to verno_app;

alter table tenants
add column if not exists schema_name text,
add column if not exists provisioned_at timestamptz,
add column if not exists db_status text not null default 'pending';

update tenants
set schema_name = coalesce(schema_name, 'tenant_' || tenant_key)
where schema_name is null;

create or replace function provision_tenant(p_tenant_key text)
returns void
language plpgsql
as $$
declare
  v_schema text := 'tenant_' || p_tenant_key;
begin
  execute format('create schema if not exists %I authorization postgres', v_schema);
  execute format('grant usage, create on schema %I to verno_app', v_schema);

  execute format('alter default privileges in schema %I grant select, insert, update, delete on tables to verno_app', v_schema);
  execute format('alter default privileges in schema %I grant usage, select, update on sequences to verno_app', v_schema);
  execute format('alter default privileges in schema %I grant execute on functions to verno_app', v_schema);

  update tenants
  set schema_name = v_schema,
      db_status = 'provisioned',
      provisioned_at = now(),
      updated_at = now()
  where tenant_key = p_tenant_key;

  if not found then
    raise exception 'tenant_key % not found in tenants table', p_tenant_key;
  end if;
end;
$$;

select provision_tenant('demo');

SQL

# -------------------------------------------------
# Test als verno_app
# -------------------------------------------------

PGPASSWORD="$(gcloud secrets versions access latest --secret=verno-db-password)" \
psql -h 127.0.0.1 -p 5433 -U verno_app -d verno_control -c \
"select schema_name from information_schema.schemata where schema_name like 'tenant_%' order by 1;"

echo "Control-DB & Tenant provisioning setup completed."