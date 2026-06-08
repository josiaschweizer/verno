package ch.verno.common.server.service.store.workspace;

import jakarta.annotation.Nonnull;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface IWorkspaceStartStatusEventServiceStore {

  @Nonnull
  SseEmitter subscribe(@Nonnull String startSessionId);

  void publish(@Nonnull WorkspaceStartSession session);

}
