create table public.tenant_billing
(
    id                         bigserial primary key,
    tenant_id                  bigint not null,
    created_at                 timestamptz not null default now(),
    updated_at                 timestamptz not null default now(),

    stripe_customer_id         varchar(255),
    stripe_subscription_id     varchar(255),

    plan_key                   varchar(100) not null,
    subscription_status        varchar(100) not null,
    payment_status             varchar(100) not null,

    current_period_end         timestamptz,
    grace_until                timestamptz,

    has_valid_payment_method   boolean not null default false,
    last_webhook_event_id      varchar(255),

    constraint fk_tenant_billing_tenant
        foreign key (tenant_id) references public.tenant(id),

    constraint uq_tenant_billing_tenant
        unique (tenant_id),

    constraint chk_tenant_billing_plan_key
        check (plan_key in ('FREE', 'BASIC', 'PRO')),

    constraint chk_tenant_billing_subscription_status
        check (subscription_status in ('INACTIVE', 'TRIAL', 'ACTIVE', 'PAST_DUE', 'CANCELED', 'BLOCKED')),

    constraint chk_tenant_billing_payment_status
        check (payment_status in ('UNPAID', 'PAID', 'FAILED', 'REQUIRES_ACTION'))
);

create index idx_tenant_billing_tenant_id
    on public.tenant_billing (tenant_id);

create index idx_tenant_billing_stripe_customer_id
    on public.tenant_billing (stripe_customer_id);

create index idx_tenant_billing_stripe_subscription_id
    on public.tenant_billing (stripe_subscription_id);



create table public.billing_access_token
(
    id            bigserial primary key,
    tenant_id     bigint not null,
    user_id       bigint not null,

    token_hash    varchar(255) not null,
    purpose       varchar(100) not null,

    expires_at    timestamptz not null,
    used_at       timestamptz,
    created_at    timestamptz not null default now(),

    constraint fk_billing_access_token_tenant
        foreign key (tenant_id) references public.tenant(id),

    constraint fk_billing_access_token_user
        foreign key (user_id) references public.app_user(id),

    constraint uq_billing_access_token_token_hash
        unique (token_hash),

    constraint chk_billing_access_token_purpose
        check (purpose in ('UPDATE_PAYMENT_METHOD', 'START_CHECKOUT', 'OPEN_BILLING_PORTAL'))
);

create index idx_billing_access_token_tenant_id
    on public.billing_access_token (tenant_id);

create index idx_billing_access_token_user_id
    on public.billing_access_token (user_id);

create index idx_billing_access_token_expires_at
    on public.billing_access_token (expires_at);

create index idx_billing_access_token_used_at
    on public.billing_access_token (used_at);



create table public.billing_webhook_event
(
    id                bigserial primary key,
    stripe_event_id   varchar(255) not null,
    event_type        varchar(255) not null,

    status            varchar(100) not null,
    processed_at      timestamptz,
    created_at        timestamptz not null default now(),

    payload_json      text,

    constraint uq_billing_webhook_event_stripe_event_id
        unique (stripe_event_id),

    constraint chk_billing_webhook_event_status
        check (status in ('RECEIVED', 'PROCESSED', 'FAILED'))
);

create index idx_billing_webhook_event_event_type
    on public.billing_webhook_event (event_type);

create index idx_billing_webhook_event_status
    on public.billing_webhook_event (status);

create index idx_billing_webhook_event_processed_at
    on public.billing_webhook_event (processed_at);