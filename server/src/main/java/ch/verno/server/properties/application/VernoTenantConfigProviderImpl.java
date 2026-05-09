package ch.verno.server.properties.application;

import ch.verno.common.properties.configprovider.VernoTenantConfigProvider;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Optional;

@ConfigurationProperties(prefix = "verno.mandant")
public class VernoTenantConfigProviderImpl implements VernoTenantConfigProvider {

  private boolean enabled = true;
  private boolean allowHeaderFallback = true;
  @Nullable private String headerName = "X-Mandant-Id";
  @Nullable private List<String> baseDomains = List.of("verno-app.ch", "a.run.app", "localhost");

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public boolean isAllowHeaderFallback() {
    return allowHeaderFallback;
  }

  public void setAllowHeaderFallback(final boolean allowHeaderFallback) {
    this.allowHeaderFallback = allowHeaderFallback;
  }

  @Nonnull
  @Override
  public String getHeaderName() {
    return Optional.ofNullable(headerName).orElse(Publ.EMPTY_STRING);
  }

  public void setHeaderName(@Nullable final String headerName) {
    this.headerName = headerName;
  }

  @Nonnull
  @Override
  public List<String> getBaseDomains() {
    return Optional.ofNullable(baseDomains).orElse(List.of());
  }

  public void setBaseDomains(@Nullable final List<String> baseDomains) {
    this.baseDomains = baseDomains;
  }
}