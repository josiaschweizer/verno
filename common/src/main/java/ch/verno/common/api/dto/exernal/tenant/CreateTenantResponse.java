package ch.verno.common.api.dto.exernal.tenant;

public record CreateTenantResponse(
        Long tenantId,
        String tenantKey,
        String subdomain,
        boolean error,
        String status
) {
}