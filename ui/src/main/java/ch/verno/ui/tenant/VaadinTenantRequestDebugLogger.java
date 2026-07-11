package ch.verno.ui.tenant;

import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class VaadinTenantRequestDebugLogger {

  private static final Logger log = LoggerFactory.getLogger(VaadinTenantRequestDebugLogger.class);

  private VaadinTenantRequestDebugLogger() {
  }

  public static void log() {
    final VaadinRequest vaadinRequest = VaadinService.getCurrentRequest();

    if (vaadinRequest instanceof VaadinServletRequest servletRequest) {
      ServletTenantRequestDebugLogger.log(
              servletRequest.getHttpServletRequest(),
              "VaadinService.getCurrentRequest()"
      );
      return;
    }

    if (vaadinRequest != null) {
      log.error("VaadinRequest present but not VaadinServletRequest: {}", vaadinRequest.getClass().getName());
    } else {
      log.error("No VaadinRequest available");
    }

    final var attributes = RequestContextHolder.getRequestAttributes();

    if (attributes instanceof ServletRequestAttributes servletRequestAttributes) {
      ServletTenantRequestDebugLogger.log(
              servletRequestAttributes.getRequest(),
              "RequestContextHolder"
      );
      return;
    }

    log.error("========== TENANT NOT SET ==========");
    log.error("No ServletRequestAttributes available.");
    log.error("Thread: {}", Thread.currentThread().getName());
    log.error("=====================================");
  }
}