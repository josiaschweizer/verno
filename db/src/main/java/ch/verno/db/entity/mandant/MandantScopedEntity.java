package ch.verno.db.entity.mandant;

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
public abstract class MandantScopedEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "mandant_id", nullable = false, updatable = false)
  private MandantEntity mandant;

  public MandantEntity getMandant() {
    return mandant;
  }

  public void setMandant(@Nonnull final MandantEntity mandant) {
    this.mandant = mandant;
  }
}