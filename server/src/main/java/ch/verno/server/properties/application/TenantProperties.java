package ch.verno.server.properties.application;

import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Optional;

@ConfigurationProperties(prefix = "verno.mandant")
public class TenantProperties {

  private boolean enabled = true;
  @Nullable private String headerName = "X-Mandant-Id";
  @Nullable private List<String> baseDomains = List.of(
          "verno-app.ch",
          "a.run.app",
          "localhost"
  );
  private boolean allowHeaderFallback = true;

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

  @Nonnull
  public String getHeaderName() {
    return Optional.ofNullable(headerName).orElse(Publ.EMPTY_STRING);
  }

  public void setHeaderName(@Nullable final String headerName) {
    this.headerName = headerName;
  }

  @Nonnull
  public List<String> getBaseDomains() {
    return Optional.ofNullable(baseDomains).orElse(List.of());
  }

  public void setBaseDomains(@Nullable final List<String> baseDomains) {
    this.baseDomains = baseDomains;
  }

  public boolean isAllowHeaderFallback() {
    return allowHeaderFallback;
  }

  public void setAllowHeaderFallback(final boolean allowHeaderFallback) {
    this.allowHeaderFallback = allowHeaderFallback;
  }
}