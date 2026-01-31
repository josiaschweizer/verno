create table if not exists app_meta (
    key text primary key,
    value text not null,
    updated_at timestamptz not null default now()
);

insert into app_meta(key, value)
values ('schema_version', '1') on conflict (key) do nothing;

create table if not exists users (
    id uuid primary key default gen_random_uuid(),
    email varchar(255) not null unique,
    display_name varchar(255) not null,
    password_hash text not null,
    status varchar(32) not null default 'active',
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create index if not exists idx_users_email on users(email);

create table if not exists roles (
    id uuid primary key default gen_random_uuid(),
    name varchar(64) not null unique
);

create table if not exists user_roles (
    user_id uuid not null references users(id) on delete cascade,
    role_id uuid not null references roles(id) on delete cascade,
    primary key (user_id, role_id)
);