package ch.verno.ui.tenant;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ServletTenantRequestDebugLogger {

  private static final Logger log = LoggerFactory.getLogger(ServletTenantRequestDebugLogger.class);

  private ServletTenantRequestDebugLogger() {
  }

  public static void log(@Nonnull final HttpServletRequest request,
                         @Nonnull final String source) {
    logHeader();
    logFromHttpServletRequest(request, source);
    logFooter();
  }

  private static void logHeader() {
    log.error("========== TENANT NOT SET ==========");
  }

  private static void logFooter() {
    log.error("=====================================");
  }

  private static void logFromHttpServletRequest(@Nonnull final HttpServletRequest request,
                                                @Nonnull final String source) {
    final var xfp = request.getHeader("X-Forwarded-Proto");
    final var xfh = request.getHeader("X-Forwarded-Host");
    final var xff = request.getHeader("X-Forwarded-For");
    final var forwarded = request.getHeader("Forwarded");

    final var scheme = xfp != null ? xfp.split(",")[0].trim() : request.getScheme();
    final var host = xfh != null ? xfh.split(",")[0].trim() : request.getServerName();

    final var uri = request.getRequestURI();
    final var query = request.getQueryString();
    final var fullUrl = scheme + "://" + host + uri + (query != null ? "?" + query : "");

    log.error("Request source   : {}", source);
    log.error("External URL     : {}", fullUrl);
    log.error("Method           : {}", request.getMethod());
    log.error("RequestURI       : {}", uri);
    log.error("QueryString      : {}", query);
    log.error("RemoteAddr       : {}", request.getRemoteAddr());
    log.error("User-Agent       : {}", request.getHeader("User-Agent"));

    log.error("Forwarded        : {}", forwarded);
    log.error("X-Forwarded-For  : {}", xff);
    log.error("X-Forwarded-Host : {}", xfh);
    log.error("X-Forwarded-Proto: {}", xfp);
  }
}