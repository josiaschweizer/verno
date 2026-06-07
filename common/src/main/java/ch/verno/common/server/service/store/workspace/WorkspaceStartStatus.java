package ch.verno.common.server.service.store.workspace;

import jakarta.annotation.Nonnull;

public enum WorkspaceStartStatus {
  STARTING("server.starting"),
  READY("server.ready"),
  FAILED("server.failed"),
  EXPIRED("server.expired"),;

  @Nonnull private final String descriptionKey;

  WorkspaceStartStatus(@Nonnull final String descriptionKey) {
    this.descriptionKey = descriptionKey;
  }

  @Nonnull
  public String getDescriptionKey() {
    return descriptionKey;
  }
}