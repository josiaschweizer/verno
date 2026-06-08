package ch.verno.common.api.dto.exernal.workspace.start;

import jakarta.annotation.Nonnull;

public record StartWorkspaceRequest(@Nonnull String tenantName) {
}
