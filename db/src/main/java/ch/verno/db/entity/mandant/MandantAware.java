package ch.verno.db.entity.mandant;

import jakarta.annotation.Nullable;

public interface MandantAware {

  @Nullable
  MandantEntity getMandant();

  void setMandant(MandantEntity mandant);
}