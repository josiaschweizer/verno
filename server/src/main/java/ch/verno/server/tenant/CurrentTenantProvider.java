package ch.verno.server.tenant;

import ch.verno.common.tenant.TenantContext;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class CurrentTenantProvider {

  @Nullable
  public Long getCurrentTenantId() {
    return TenantContext.get();
  }

  @Nonnull
  public Long getRequiredTenantId() {
    return TenantContext.getRequired();
  }

  public boolean hasCurrentTenant() {
    return TenantContext.get() != null;
  }
}