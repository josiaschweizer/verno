package ch.verno.server.service.extern.workspace;

import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.lib.i18n.TranslationHelper;
import ch.verno.common.server.service.extern.workspace.IWorkspaceRunnerService;
import ch.verno.common.server.service.store.workspace.IWorkspaceSessionStartStore;
import ch.verno.common.server.service.store.workspace.IWorkspaceStartStatusEventServiceStore;
import ch.verno.common.server.service.store.workspace.WorkspaceStartSession;
import ch.verno.common.server.service.store.workspace.WorkspaceStartStatus;
import ch.verno.lib.Lazy;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class WorkspaceRunnerService implements IWorkspaceRunnerService {

  private static final int MAX_ATTEMPTS = 30;
  private static final Duration POLL_INTERVAL = Duration.ofSeconds(2);
  private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);

  @Nonnull private final GlobalInterface globalInterface;
  @Nonnull private final Lazy<IWorkspaceSessionStartStore> workspaceSessionStartStore;
  @Nonnull private final Lazy<IWorkspaceStartStatusEventServiceStore> workspaceStartStatusEventServiceStore;
  @Nonnull private final HttpClient httpClient;

  public WorkspaceRunnerService(@Nonnull final GlobalInterface globalInterface) {
    this.globalInterface = globalInterface;
    this.workspaceSessionStartStore = Lazy.of(() -> globalInterface.getService(IWorkspaceSessionStartStore.class));
    this.workspaceStartStatusEventServiceStore = Lazy.of(() -> globalInterface.getService(IWorkspaceStartStatusEventServiceStore.class));
    this.httpClient = HttpClient.newBuilder()
            .connectTimeout(REQUEST_TIMEOUT)
            .build();
  }

  @Async
  @Override
  public void startAsync(@Nonnull final String startSessionId) {
    final var sessionOptional = workspaceSessionStartStore.get().find(startSessionId);
    if (sessionOptional.isEmpty()) {
      return;
    }

    final var session = sessionOptional.get();
    updateStatus(session, WorkspaceStartStatus.STARTING);
    final var healthUri = buildHealthUri(session.redirectUrl());

    try {
      for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
        if (isReady(healthUri)) {
          updateStatus(session, WorkspaceStartStatus.READY);
          return;
        }

        Thread.sleep(POLL_INTERVAL.toMillis());
      }
      updateStatus(session, WorkspaceStartStatus.FAILED);
    } catch (final InterruptedException e) {
      Thread.currentThread().interrupt();
      updateStatus(session, WorkspaceStartStatus.FAILED);
    } catch (final Exception e) {
      updateStatus(session, WorkspaceStartStatus.FAILED);
    }
  }

  @Nonnull
  private URI buildHealthUri(@Nonnull final String redirectUrl) {
    final var normalizedRedirectUrl = redirectUrl.endsWith(Publ.SLASH)
            ? redirectUrl
            : redirectUrl + Publ.SLASH; // we ping base url + / to wake up the google cloud runner

    return URI.create(normalizedRedirectUrl);
  }

  private boolean isReady(@Nonnull final URI healthUri) {
    try {
      final var request = HttpRequest.newBuilder()
              .uri(healthUri)
              .timeout(REQUEST_TIMEOUT)
              .GET()
              .build();

      final var response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
      return response.statusCode() >= 200 && response.statusCode() < 500;
    } catch (final Exception e) {
      return false;
    }
  }

  private void updateStatus(@Nonnull final WorkspaceStartSession session,
                            @Nonnull final WorkspaceStartStatus status) {
    final var updatedSession = workspaceSessionStartStore.get().updateStatus(
            session.startSessionId(),
            status,
            TranslationHelper.getTranslation(
                    globalInterface,
                    status.getDescriptionKey()
            )

    );

    updatedSession.ifPresent((s) -> workspaceStartStatusEventServiceStore.get().publish(s));
  }
}