begin;

create table if not exists public.mail_config (
    tenant_id          bigint primary key,
    enabled            boolean not null default true,

    from_name          text not null,
    from_email         text not null,
    reply_to_email     text null,
    default_bcc        text null,

    smtp_host          text not null,
    smtp_port          int  not null,
    smtp_username      text not null,
    smtp_password_b64  text not null,
    smtp_security      text not null,
    smtp_auth          boolean not null default true,

    created_at         timestamptz not null default now(),
    updated_at         timestamptz not null default now(),

    constraint smtp_password_b64_format
    check (smtp_password_b64 ~ '^[A-Za-z0-9+/]+={0,2}$')
    );

create table if not exists public.mail_template_type (
    key text primary key
);

insert into public.mail_template_type (key) values
    ('WELCOME'),
    ('COURSE_INVITE'),
    ('COURSE_REMINDER'),
    ('PASSWORD_RESET')
    on conflict (key) do nothing;

create table if not exists public.mail_template (
    tenant_id        bigint not null,
    template_key     text   not null,

    subject          text   not null,
    content          text   not null,

    content_format   text   not null default 'AUTO',

    created_at       timestamptz not null default now(),
    updated_at       timestamptz not null default now(),

    primary key (tenant_id, template_key),

    constraint fk_mail_template_type
    foreign key (template_key)
    references public.mail_template_type(key)
    );

create index if not exists idx_mail_template_tenant
    on public.mail_template (tenant_id);

commit;