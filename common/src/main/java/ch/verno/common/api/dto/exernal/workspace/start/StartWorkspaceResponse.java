package ch.verno.common.api.dto.exernal.workspace.start;

import jakarta.annotation.Nonnull;

public record StartWorkspaceResponse(
        @Nonnull String tenantSlug,
        @Nonnull String tenantName,
        @Nonnull String startSessionId
) {

  @Nonnull
  public static StartWorkspaceResponse of(@Nonnull final String tenantSlug,
                                          @Nonnull final String tenantName,
                                          @Nonnull final String startSessionId) {
    return new StartWorkspaceResponse(tenantSlug, tenantName, startSessionId);
  }
}