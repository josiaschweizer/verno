package ch.verno.provisioner.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TenantStatusResponse(
        UUID id,
        String tenantKey,
        String tenantName,
        String subdomain,
        String preferredLanguage,
        String status,
        String schemaName,
        String dbStatus,
        OffsetDateTime provisionedAt,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        String runServiceName,
        String url
) {}