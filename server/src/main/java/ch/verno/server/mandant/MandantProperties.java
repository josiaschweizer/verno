package ch.verno.server.mandant;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "verno.mandant")
public class MandantProperties {

  private boolean enabled = true;

  private String headerName = "X-Mandant-Id";

  private List<String> baseDomains = List.of(
          "verno-app.ch",
          "localhost"
  );

  private boolean allowHeaderFallback = true;

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

  public String getHeaderName() {
    return headerName;
  }

  public void setHeaderName(final String headerName) {
    this.headerName = headerName;
  }

  public List<String> getBaseDomains() {
    return baseDomains;
  }

  public void setBaseDomains(final List<String> baseDomains) {
    this.baseDomains = baseDomains;
  }

  public boolean isAllowHeaderFallback() {
    return allowHeaderFallback;
  }

  public void setAllowHeaderFallback(final boolean allowHeaderFallback) {
    this.allowHeaderFallback = allowHeaderFallback;
  }
}