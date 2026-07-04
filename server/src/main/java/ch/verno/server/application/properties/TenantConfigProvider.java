package ch.verno.server.application.properties;

import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConfigurationProperties(prefix = "verno.mandant")
public class TenantConfigProvider {

  private boolean enabled = true;
  private boolean allowHeaderFallback = true;
  @Nullable private String headerName = "X-Mandant-Id";

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isAllowHeaderFallback() {
    return allowHeaderFallback;
  }

  public void setAllowHeaderFallback(final boolean allowHeaderFallback) {
    this.allowHeaderFallback = allowHeaderFallback;
  }

  @Nonnull
  public String getHeaderName() {
    return Optional.ofNullable(headerName).orElse(Publ.EMPTY_STRING);
  }

  public void setHeaderName(@Nullable final String headerName) {
    this.headerName = headerName;
  }
}