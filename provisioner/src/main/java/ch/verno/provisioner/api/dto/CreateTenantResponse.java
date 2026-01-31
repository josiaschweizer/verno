package ch.verno.provisioner.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateTenantResponse(
        UUID tenantId,
        String tenantKey,
        String schemaName,
        String status,
        String dbStatus,
        OffsetDateTime provisionedAt,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}