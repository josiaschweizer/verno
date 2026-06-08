package ch.verno.api.endpoints.external;

import ch.verno.api.base.BaseController;
import ch.verno.common.api.dto.exernal.workspace.start.StartWorkspaceRequest;
import ch.verno.common.api.dto.exernal.workspace.start.StartWorkspaceResponse;
import ch.verno.common.api.dto.exernal.workspace.status.WorkspaceStartStatusResponse;
import ch.verno.common.api.util.ApiErrorResponse;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.lib.i18n.TranslationHelper;
import ch.verno.common.server.service.extern.workspace.IWorkspaceRunnerService;
import ch.verno.common.server.service.intern.tenant.ITenantService;
import ch.verno.common.server.service.store.workspace.IWorkspaceSessionStartStore;
import ch.verno.common.server.service.store.workspace.IWorkspaceStartStatusEventServiceStore;
import ch.verno.common.server.service.store.workspace.WorkspaceStartSession;
import ch.verno.common.server.service.store.workspace.WorkspaceStartStatus;
import ch.verno.lib.Lazy;
import ch.verno.publ.ApiUrl;
import ch.verno.publ.VernoSecrets;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping(ApiUrl.WORKSPACE)
public class WorkspaceController extends BaseController {

  @NonNls private static final String NOT_FOUND_CODE = "TENANT_NOT_FOUND";
  @NonNls private static final String START_SESSION_NOT_FOUND = "START_SESSION_NOT_FOUND";
  private static final String NOT_FOUND_MESSAGE = "There is no tenant with the entered name.";
  private static final String START_SESSION_NOT_FOUND_MESSAGE = "The workspace start session was not found.";

  @Nonnull private final GlobalInterface globalInterface;
  @Nonnull private final Lazy<ITenantService> tenantService;
  @Nonnull private final Lazy<IWorkspaceRunnerService> workspaceRunnerService;
  @Nonnull private final Lazy<IWorkspaceSessionStartStore> workspaceSessionStartStore;
  @Nonnull private final Lazy<IWorkspaceStartStatusEventServiceStore> workspaceStartStatusEventService;

  public WorkspaceController(@Nonnull final GlobalInterface globalInterface) {
    this.globalInterface = globalInterface;
    this.tenantService = Lazy.of(() -> globalInterface.getService(ITenantService.class));
    this.workspaceRunnerService = Lazy.of(() -> globalInterface.getService(IWorkspaceRunnerService.class));
    this.workspaceSessionStartStore = Lazy.of(() -> globalInterface.getService(IWorkspaceSessionStartStore.class));
    this.workspaceStartStatusEventService = Lazy.of(() -> globalInterface.getService(IWorkspaceStartStatusEventServiceStore.class));
  }

  @Nonnull
  @PostMapping(ApiUrl.WORKSPACE_START)
  public ResponseEntity<?> startWorkspace(@RequestBody @Nonnull final StartWorkspaceRequest request) {
    final var tenantIdByName = tenantService.get().findTenantIdByName(request.tenantName());
    if (tenantIdByName.isEmpty()) {
      return getNotFoundResponse();
    }

    final var tenantId = tenantIdByName.get();
    final var tenantOptional = tenantService.get().findById(tenantId);
    if (tenantOptional.isEmpty()) {
      return getNotFoundResponse();
    }

    final var tenant = tenantOptional.get();
    final var redirectUrl = String.format(
            globalInterface.getEnvProperties().getEnv(VernoSecrets.VERNO_BASE_URL_PATTERN),
            tenant.getSlug()
    );

    final var sessionStart = new WorkspaceStartSession(
            UUID.randomUUID().toString(),
            tenantId,
            tenant.getName(),
            tenant.getSlug(),
            redirectUrl,
            WorkspaceStartStatus.STARTING,
            TranslationHelper.getTranslation(globalInterface, WorkspaceStartStatus.STARTING.getDescriptionKey()),
            Instant.now(),
            Instant.now()
    );

    workspaceSessionStartStore.get().create(sessionStart);
    workspaceRunnerService.get().startAsync(sessionStart.startSessionId());


    return ResponseEntity.ok(
            new StartWorkspaceResponse(
                    tenant.getSlug(),
                    tenant.getName(),
                    sessionStart.startSessionId()
            )
    );
  }

  @Nonnull
  private ResponseEntity<?> getNotFoundResponse() {
    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ApiErrorResponse(
                    NOT_FOUND_CODE,
                    NOT_FOUND_MESSAGE
            ));
  }

  @Nonnull
  @GetMapping(ApiUrl.WORKSPACE_SESSION_STATUS)
  public ResponseEntity<?> getWorkspaceStartStatus(@PathVariable @Nonnull final String startSessionId) {
    final var sessionOptional = workspaceSessionStartStore.get().find(startSessionId);

    if (sessionOptional.isEmpty()) {
      return ResponseEntity
              .status(HttpStatus.NOT_FOUND)
              .body(new ApiErrorResponse(
                      START_SESSION_NOT_FOUND,
                      START_SESSION_NOT_FOUND_MESSAGE
              ));
    }

    final var session = sessionOptional.get();

    return ResponseEntity.ok(
            new WorkspaceStartStatusResponse(
                    session.startSessionId(),
                    session.tenantName(),
                    session.tenantSlug(),
                    session.redirectUrl(),
                    session.status(),
                    session.message()
            )
    );
  }

  @Nonnull
  @GetMapping(value = ApiUrl.WORKSPACE_SESSION_EVENTS, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribeToWorkspaceStartStatus(@PathVariable @Nonnull final String startSessionId) {
    final var emitter = workspaceStartStatusEventService.get().subscribe(startSessionId);

    workspaceSessionStartStore.get()
            .find(startSessionId)
            .ifPresent(session -> workspaceStartStatusEventService.get().publish(session));

    return emitter;
  }

}