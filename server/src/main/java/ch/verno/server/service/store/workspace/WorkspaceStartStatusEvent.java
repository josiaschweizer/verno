package ch.verno.server.service.store.workspace;

import jakarta.annotation.Nonnull;

import javax.annotation.Nullable;

public record WorkspaceStartStatusEvent(
        @Nonnull String startSessionId,
        @Nonnull String tenantName,
        @Nonnull String tenantSlug,
        @Nonnull String redirectUrl,
        @Nonnull String status,
        @Nullable String message) {
}