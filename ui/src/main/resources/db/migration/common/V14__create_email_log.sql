create table mail_log (
    id                    bigserial primary key,
    tenant_id             bigint not null,

    recipient_email       varchar(320) not null,
    recipient_name        varchar(200),

    template_name         varchar(200),

    subject               varchar(500) not null,
    content               text not null,

    placeholders          jsonb,

    status                varchar(20) not null,
    error_message         text,

    provider_message_id   varchar(200),

    sent_at               timestamp,
    created_at            timestamp not null default now(),

    created_by            bigint
);