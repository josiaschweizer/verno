package ch.verno.common.server.service.extern.workspace;

import jakarta.annotation.Nonnull;

public interface IWorkspaceRunnerService {

  void startAsync(@Nonnull String startSessionId);

}
