package ch.verno.ui.base.os;

import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.Nonnull;

public class OSUtil {

  private OSUtil() {
  }

  /**
   * detect the current OS
   *
   * @return the current operating system by using the vaadin user agent -
   * default is windows (is the default return from OS.getFromKey())
   */
  @Nonnull
  public static OS getOs() {
    final var browser = VaadinSession.getCurrent().getBrowser();

    final var userAgent = browser.getUserAgent();
    if (userAgent == null || userAgent.isBlank()) {
      return OS.getDefault();
    }

    return OS.fromUserAgent(userAgent);
  }

}
