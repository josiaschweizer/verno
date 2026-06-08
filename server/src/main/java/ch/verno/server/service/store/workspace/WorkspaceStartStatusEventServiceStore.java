package ch.verno.server.service.store.workspace;

import ch.verno.common.server.service.store.workspace.IWorkspaceStartStatusEventServiceStore;
import ch.verno.common.server.service.store.workspace.WorkspaceStartSession;
import ch.verno.publ.SsEventConstants;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WorkspaceStartStatusEventServiceStore implements IWorkspaceStartStatusEventServiceStore {

  private static final long TIMEOUT = 120_000L;

  @Nonnull private final Map<String, SseEmitter> emitters;

  public WorkspaceStartStatusEventServiceStore() {
    this.emitters = new ConcurrentHashMap<>();
  }

  @Nonnull
  @Override
  public SseEmitter subscribe(@Nonnull final String startSessionId) {
    final var emitter = new SseEmitter(TIMEOUT);
    emitters.put(startSessionId, emitter);
    emitter.onCompletion(() -> emitters.remove(startSessionId));
    emitter.onTimeout(() -> emitters.remove(startSessionId));
    emitter.onError(e -> emitters.remove(startSessionId));
    return emitter;
  }

  @Override
  public void publish(@Nonnull final WorkspaceStartSession session) {
    final var emitter = emitters.get(session.startSessionId());
    if (emitter == null) {
      return;
    }

    try {
      emitter.send(SseEmitter.event()
              .name(SsEventConstants.WORKSPACE_STATUS)
              .data(new WorkspaceStartStatusEvent(
                      session.startSessionId(),
                      session.tenantName(),
                      session.tenantSlug(),
                      session.redirectUrl(),
                      session.status().name(),
                      session.message()
              )));

      switch (session.status()) {
        case READY, FAILED, EXPIRED -> {
          emitter.complete();
          emitters.remove(session.startSessionId());
        }
        case STARTING -> {
          // keep connection open
        }
      }
    } catch (final IOException e) {
      emitter.completeWithError(e);
      emitters.remove(session.startSessionId());
    }
  }
}