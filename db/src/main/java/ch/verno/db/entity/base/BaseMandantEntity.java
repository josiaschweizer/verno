package ch.verno.db.entity.base;

import ch.verno.db.entity.mandant.MandantEntity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseMandantEntity {

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "mandant_id", nullable = false, updatable = false)
  private MandantEntity mandant;

  public MandantEntity getMandant() {
    return mandant;
  }

  public void setMandant(MandantEntity mandant) {
    this.mandant = mandant;
  }
}