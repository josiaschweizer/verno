create table public.tenant_billing_licence_option
(
    tenant_billing_id bigint       not null,
    licence_option    varchar(255) not null,

    constraint fk_tenant_billing_licence_option_billing
        foreign key (tenant_billing_id)
            references public.tenant_billing (id)
            on delete cascade,

    constraint uq_tenant_billing_licence_option
        unique (tenant_billing_id, licence_option)
);