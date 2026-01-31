package ch.verno.provisioner.db.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TenantRecord(
        UUID id,
        String tenantKey,
        String tenantName,
        String tenantSubdomain,
        String preferredLanguage,
        String status,
        String schemaName,
        OffsetDateTime provisionedAt,
        String dbStatus,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}