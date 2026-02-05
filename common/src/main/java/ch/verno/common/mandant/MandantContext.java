package ch.verno.common.mandant;

import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class MandantContext {

  private static final Logger log = LoggerFactory.getLogger(MandantContext.class);
  private static final ThreadLocal<Long> CURRENT = new ThreadLocal<>();

  private MandantContext() {
  }

  public static void set(@Nonnull final Long mandantId) {
    CURRENT.set(mandantId);
  }

  @Nullable
  public static Long get() {
    return CURRENT.get();
  }

  @Nonnull
  public static Long getRequired() {
    final Long id = CURRENT.get();

    if (id == null) {
      logRequestDebug();
      throw new IllegalStateException("No mandant set for request");
    }

    return id;
  }

  public static void clear() {
    CURRENT.remove();
  }

  /**
   * Logs everything relevant to understand why mandant resolution fails.
   * Works locally and on Cloud Run.
   */
  private static void logRequestDebug() {
    logConfirmHeader();

    final VaadinRequest vaadinRequest = VaadinService.getCurrentRequest();
    if (vaadinRequest instanceof VaadinServletRequest vsr) {
      logFromHttpServletRequest(vsr.getHttpServletRequest(), "VaadinService.getCurrentRequest()");
      logConfirmFooter();
      return;
    }

    if (vaadinRequest != null) {
      log.error("VaadinRequest present but not VaadinServletRequest: {}", vaadinRequest.getClass().getName());
    } else {
      log.error("No VaadinRequest available");
    }

    final RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
    if (attrs instanceof ServletRequestAttributes sra) {
      logFromHttpServletRequest(sra.getRequest(), "RequestContextHolder");
      logConfirmFooter();
      return;
    }

    log.error("No ServletRequestAttributes available (RequestContextHolder is null).");
    log.error("Thread: {}", Thread.currentThread().getName());
    logConfirmFooter();
  }

  private static void logConfirmHeader() {
    log.error("========== MANDANT NOT SET ==========");
  }

  private static void logConfirmFooter() {
    log.error("=====================================");
  }

  private static void logFromHttpServletRequest(HttpServletRequest req, String source) {
    final String xfp = req.getHeader("X-Forwarded-Proto");
    final String xfh = req.getHeader("X-Forwarded-Host");
    final String xff = req.getHeader("X-Forwarded-For");
    final String forwarded = req.getHeader("Forwarded");

    final String scheme = (xfp != null) ? xfp.split(",")[0].trim() : req.getScheme();
    final String host = (xfh != null) ? xfh.split(",")[0].trim() : req.getServerName();

    final String uri = req.getRequestURI();
    final String query = req.getQueryString();
    final String fullUrl = scheme + "://" + host + uri + (query != null ? "?" + query : "");

    log.error("Request source   : {}", source);
    log.error("External URL     : {}", fullUrl);
    log.error("Method           : {}", req.getMethod());
    log.error("RequestURI       : {}", uri);
    log.error("QueryString      : {}", query);
    log.error("RemoteAddr       : {}", req.getRemoteAddr());
    log.error("User-Agent       : {}", req.getHeader("User-Agent"));

    log.error("Forwarded        : {}", forwarded);
    log.error("X-Forwarded-For  : {}", xff);
    log.error("X-Forwarded-Host : {}", xfh);
    log.error("X-Forwarded-Proto: {}", xfp);
  }
}