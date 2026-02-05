package ch.verno.common.api.dto;

public record CreateTenantResponse(
        Long tenantId,
        String tenantKey,
        String subdomain,
        String status
) {
}