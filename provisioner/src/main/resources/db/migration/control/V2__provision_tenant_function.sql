create or replace function provision_tenant(
    p_tenant_key text,
    p_admin_email text,
    p_admin_display_name text,
    p_admin_password_hash text
) returns void
language plpgsql
as $$
declare
v_schema_name text;
begin
    if p_tenant_key is null or btrim(p_tenant_key) = '' then
        raise exception 'tenant_key must not be empty';
end if;

    v_schema_name := 'tenant_' || p_tenant_key;

update tenants
set status = 'provisioning',
    db_status = 'provisioning',
    updated_at = now()
where tenant_key = p_tenant_key;

if not found then
        raise exception 'tenant_key not found: %', p_tenant_key;
end if;

execute format('create schema if not exists %I', v_schema_name);

execute format('grant usage on schema %I to verno_app', v_schema_name);
execute format('grant create on schema %I to verno_app', v_schema_name);

execute format($sql$
                   alter default privileges in schema %I
        grant select, insert, update, delete on tables to verno_app
    $sql$, v_schema_name);

execute format($sql$
                   alter default privileges in schema %I
        grant usage, select, update on sequences to verno_app
    $sql$, v_schema_name);

execute format($sql$
                   create table if not exists %I.app_meta (
            key text primary key,
            value text not null,
            updated_at timestamptz not null default now()
        )
    $sql$, v_schema_name);

execute format($sql$
                   insert into %I.app_meta(key, value)
        values ('schema_version', '1')
        on conflict (key) do nothing
    $sql$, v_schema_name);

execute format($sql$
    create table if not exists %I.users (
    id uuid primary key default gen_random_uuid(),
    email varchar(255) not null unique,
    display_name varchar(255) not null,
    password_hash varchar(255) not null,
    status varchar(32) not null default 'active',
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
)
    $sql$, v_schema_name);

execute format($sql$
                   create index if not exists idx_users_email on %I.users(email)
    $sql$, v_schema_name);

execute format($sql$
    create table if not exists %I.roles (
    id uuid primary key default gen_random_uuid(),
    name varchar(64) not null unique
)
    $sql$, v_schema_name);

execute format($sql$
    insert into %I.roles(name)
    values ('ADMIN'), ('USER')
       on conflict do nothing
       $sql$, v_schema_name);

execute format($sql$
    create table if not exists %I.user_roles (
        user_id uuid not null references %I.users(id) on delete cascade,
        role_id uuid not null references %I.roles(id) on delete cascade,
        primary key (user_id, role_id)
    )
    $sql$, v_schema_name, v_schema_name, v_schema_name);

if p_admin_email is not null and btrim(p_admin_email) <> '' then
    if p_admin_password_hash is null or btrim(p_admin_password_hash) = '' then
    raise exception 'admin_password_hash must not be empty when admin_email is provided';
end if;

    if p_admin_display_name is null or btrim(p_admin_display_name) = '' then
    raise exception 'admin_display_name must not be empty when admin_email is provided';
end if;

execute format($sql$
    insert into %I.users (email, display_name, password_hash)
    values ($1, $2, $3)
    on conflict (email) do update
    set display_name = excluded.display_name,
    password_hash = excluded.password_hash,
    updated_at = now()
    $sql$, v_schema_name)
    using p_admin_email, p_admin_display_name, p_admin_password_hash;

execute format($sql$
    insert into %I.user_roles (user_id, role_id)
    select u.id, r.id
    from %I.users u
    join %I.roles r on r.name = 'ADMIN'
    where u.email = $1
    on conflict do nothing
    $sql$, v_schema_name, v_schema_name, v_schema_name)
    using p_admin_email;
end if;

update tenants
set schema_name = v_schema_name,
    status = 'provisioned',
    db_status = 'ready',
    provisioned_at = now(),
    updated_at = now()
where tenant_key = p_tenant_key;

end;
$$;