package ch.verno.ui.base.error;

import ch.verno.common.exceptions.server.tenant.TenantNotResolvedException;
import ch.verno.publ.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalErrorHandler implements ErrorHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalErrorHandler.class);

  @Override
  public void error(@Nonnull final ErrorEvent event) {
    log.debug("GlobalErrorHandler invoked with event: {}", event);

    final var throwable = event.getThrowable();
    final var root = rootCause(throwable);

    log.debug("GlobalErrorHandler root cause: {}", root.getClass().getName());

    if (root instanceof TenantNotResolvedException) {
      final var ui = UI.getCurrent();
      if (ui != null) {
        ui.access(() -> ui.navigate(Routes.TENANT_NOT_FOUND));
      } else {
        log.warn("TenantNotResolvedException occurred but UI.getCurrent() returned null - cannot navigate");
      }

      return;
    }

    log.error("Uncaught UI error", throwable);
  }

  @Nonnull
  private Throwable rootCause(@Nonnull final Throwable throwable) {
    Throwable current = throwable;
    while (current.getCause() != null && current.getCause() != current) {
      current = current.getCause();
    }
    return current;
  }
}