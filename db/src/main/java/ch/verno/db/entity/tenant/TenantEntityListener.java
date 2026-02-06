package ch.verno.db.entity.tenant;

import ch.verno.common.tenant.TenantContext;
import jakarta.annotation.Nonnull;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class TenantEntityListener {

  @PrePersist
  public void prePersist(@Nonnull final Object entity) {
    if (!(entity instanceof TenantScopedEntity scoped)) {
      return;
    }

    if (scoped.getTenant() == null) {
      scoped.setTenant(TenantEntity.ref(TenantContext.getRequired()));
      return;
    }

    validate(scoped, TenantContext.getRequired());
  }

  @PreUpdate
  public void preUpdate(@Nonnull final Object entity) {
    if (!(entity instanceof TenantScopedEntity scoped)) {
      return;
    }

    final long tenantContextId = TenantContext.getRequired();
    validate(scoped, tenantContextId);
  }

  private void validate(@Nonnull final TenantScopedEntity scoped,
                        @Nonnull final Long tenantContextId) {
    final Long entityTenantId = scoped.getTenant().getId();

    if (!entityTenantId.equals(tenantContextId)) {
      throw new IllegalStateException("Tenant mismatch: entity=" + entityTenantId + ", context=" + tenantContextId);
    }
  }
}