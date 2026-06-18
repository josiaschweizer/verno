package ch.verno.contract.response.tenant.create;

import jakarta.annotation.Nonnull;

public record CreateTenantResponse(
        @Nonnull Long tenantId,
        @Nonnull String tenantKey,
        @Nonnull String subdomain,
        boolean error,
        @Nonnull String status
) {
}