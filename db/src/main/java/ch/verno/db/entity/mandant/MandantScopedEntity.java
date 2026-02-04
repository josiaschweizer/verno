package ch.verno.db.entity.mandant;

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
        condition = "mandant = :mandantId"
)
public abstract class MandantScopedEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "mandant", nullable = false, updatable = false)
  private MandantEntity mandant;

  public MandantEntity getMandant() {
    return mandant;
  }

  public void setMandant(final MandantEntity mandant) {
    this.mandant = mandant;
  }
}