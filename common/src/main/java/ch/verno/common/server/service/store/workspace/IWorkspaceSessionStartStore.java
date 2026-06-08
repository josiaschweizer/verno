package ch.verno.common.server.service.store.workspace;

import jakarta.annotation.Nonnull;

import javax.annotation.Nullable;
import java.util.Optional;

public interface IWorkspaceSessionStartStore {

  void create(@Nonnull WorkspaceStartSession session);

  @Nonnull
  Optional<WorkspaceStartSession> find(@Nonnull String startSessionId);

  Optional<WorkspaceStartSession> updateStatus(@Nonnull String startSessionId,
                    @Nonnull WorkspaceStartStatus status,
                    @Nullable String message);
}
