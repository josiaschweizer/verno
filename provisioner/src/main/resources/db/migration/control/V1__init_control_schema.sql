create extension if not exists pgcrypto;

create table if not exists tenants (
    id uuid primary key default gen_random_uuid(),

    tenant_key varchar(63) not null unique,
    tenant_name varchar(255) not null,
    tenant_subdomain varchar(63) not null,
    preferred_language varchar(8) not null,

    status varchar(32) not null default 'new',
    schema_name varchar(128) null,
    provisioned_at timestamptz null,
    db_status varchar(32) not null default 'pending',

    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
    );

create index if not exists idx_tenants_status on tenants(status);
create index if not exists idx_tenants_created_at on tenants(created_at desc);

create table if not exists tenant_provision_runs (
     id bigserial primary key,
     tenant_key varchar(63) not null,
     run_id uuid not null,
     status varchar(32) not null,
     started_at timestamptz not null default now(),
     finished_at timestamptz null,
     error text null
);

create index if not exists idx_tpr_tenant_key on tenant_provision_runs(tenant_key);
create index if not exists idx_tpr_run_id on tenant_provision_runs(run_id);

create table if not exists app_meta (
    id bigserial primary key,
    key text not null unique,
    value text not null
);

insert into app_meta(key, value)
values ('seed_version', '1') on conflict (key) do update set value = excluded.value;