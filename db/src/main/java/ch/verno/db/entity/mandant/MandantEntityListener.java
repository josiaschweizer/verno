package ch.verno.db.entity.mandant;

import ch.verno.common.mandant.MandantContext;
import jakarta.annotation.Nonnull;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class MandantEntityListener {

  @PrePersist
  public void prePersist(@Nonnull final Object entity) {
    if (!(entity instanceof MandantScopedEntity scoped)) {
      return;
    }

    if (scoped.getMandant() == null) {
      scoped.setMandant(MandantEntity.ref(MandantContext.getRequired()));
      return;
    }

    validate(scoped, MandantContext.getRequired());
  }

  @PreUpdate
  public void preUpdate(@Nonnull final Object entity) {
    if (!(entity instanceof MandantScopedEntity scoped)) {
      return;
    }

    final long ctxMandantId = MandantContext.getRequired();
    validate(scoped, ctxMandantId);
  }

  private void validate(@Nonnull final MandantScopedEntity scoped,
                        @Nonnull final Long contextMandantId) {
    final Long entityMandantId = scoped.getMandant().getId();

    if (!entityMandantId.equals(contextMandantId)) {
      throw new IllegalStateException("Mandant mismatch: entity=" + entityMandantId + ", context=" + contextMandantId);
    }
  }
}