package ch.verno.server.service.store.workspace;

import ch.verno.common.server.service.store.workspace.IWorkspaceSessionStartStore;
import ch.verno.common.server.service.store.workspace.WorkspaceStartSession;
import ch.verno.common.server.service.store.workspace.WorkspaceStartStatus;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WorkspaceSessionStartStore implements IWorkspaceSessionStartStore {

  @Nonnull private final Map<String, WorkspaceStartSession> sessions;

  public WorkspaceSessionStartStore() {
    this.sessions = new ConcurrentHashMap<>();
  }

  @Override
  public void create(@Nonnull final WorkspaceStartSession session) {
    sessions.put(session.startSessionId(), session);
  }

  @Nonnull
  @Override
  public Optional<WorkspaceStartSession> find(@Nonnull final String startSessionId) {
    return Optional.ofNullable(sessions.get(startSessionId));
  }

  @Override
  public Optional<WorkspaceStartSession> updateStatus(@Nonnull final String startSessionId,
                           @Nonnull final WorkspaceStartStatus status,
                           @Nullable final String message) {
    final var existing = sessions.get(startSessionId);
    if (existing == null) {
      return Optional.empty();
    }

    final var updatedSession = new WorkspaceStartSession(
            existing.startSessionId(),
            existing.tenantId(),
            existing.tenantName(),
            existing.tenantSlug(),
            existing.redirectUrl(),
            status,
            message,
            existing.createdAt(),
            Instant.now()
    );

    sessions.put(startSessionId, updatedSession);
    return Optional.of(updatedSession);
  }
}