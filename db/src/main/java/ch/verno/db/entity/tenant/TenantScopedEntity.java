package ch.verno.db.entity.tenant;

import jakarta.annotation.Nonnull;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@MappedSuperclass
@FilterDef(
        name = "mandantFilter",
        parameters = @ParamDef(name = "mandantId", type = Long.class)
)
@Filter(
        name = "mandantFilter",
        condition = "mandant_id = :mandantId"
)
public abstract class TenantScopedEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "mandant_id", nullable = false, updatable = false)
  private TenantEntity tenant;

  public TenantEntity getTenant() {
    return tenant;
  }

  public void setTenant(@Nonnull final TenantEntity tenant) {
    this.tenant = tenant;
  }
}