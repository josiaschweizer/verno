package ch.verno.common.properties.configprovider;

import jakarta.annotation.Nonnull;

import java.util.List;

public interface VernoTenantConfigProvider {

  boolean isEnabled();

  boolean isAllowHeaderFallback();

  @Nonnull
  String getHeaderName();

  @Nonnull
  List<String> getBaseDomains();

}
