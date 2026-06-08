package ch.verno.common.server.service.store.workspace;

import jakarta.annotation.Nonnull;

import javax.annotation.Nullable;
import java.time.Instant;

public record WorkspaceStartSession(
        @Nonnull String startSessionId,
        @Nonnull Long tenantId,
        @Nonnull String tenantName,
        @Nonnull String tenantSlug,
        @Nonnull String redirectUrl,
        @Nonnull WorkspaceStartStatus status,
        @Nullable String message,
        @Nonnull Instant createdAt,
        @Nonnull Instant updatedAt

) {

}