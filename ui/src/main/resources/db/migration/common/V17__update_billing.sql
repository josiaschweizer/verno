alter table public.tenant_billing
    rename column tenant_id to mandant_id;

alter index if exists public.idx_tenant_billing_tenant_id
    rename to idx_tenant_billing_mandant_id;

alter table public.tenant_billing
    rename constraint fk_tenant_billing_tenant to fk_tenant_billing_mandant;

alter table public.tenant_billing
    rename constraint uq_tenant_billing_tenant to uq_tenant_billing_mandant;


alter table public.billing_access_token
    rename column tenant_id to mandant_id;

alter index if exists public.idx_billing_access_token_tenant_id
    rename to idx_billing_access_token_mandant_id;

alter table public.billing_access_token
    rename constraint fk_billing_access_token_tenant to fk_billing_access_token_mandant;