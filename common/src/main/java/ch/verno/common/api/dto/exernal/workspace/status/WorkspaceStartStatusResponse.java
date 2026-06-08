package ch.verno.common.api.dto.exernal.workspace.status;

import ch.verno.common.server.service.store.workspace.WorkspaceStartStatus;
import jakarta.annotation.Nonnull;

import javax.annotation.Nullable;

public record WorkspaceStartStatusResponse(
        @Nonnull String startSessionId,
        @Nonnull String tenantName,
        @Nonnull String tenantSlug,
        @Nonnull String redirectUrl,
        @Nonnull WorkspaceStartStatus status,
        @Nullable String message) {

}