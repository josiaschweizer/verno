package ch.verno.common.api.dto.exernal;

public record CreateTenantResponse(
        Long tenantId,
        String tenantKey,
        String subdomain,
        boolean error,
        String status
) {
}