package ch.verno.db.entity.mandant;

import ch.verno.common.mandant.MandantContext;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class MandantEntityListener {

  @PrePersist
  public void prePersist(final Object entity) {
    if (!(entity instanceof MandantScopedEntity scoped)) {
      return;
    }

    final long ctxMandantId = MandantContext.getRequired();

    if (scoped.getMandant() == null) {
      scoped.setMandant(MandantEntity.ref(ctxMandantId));
      return;
    }

    validate(scoped, ctxMandantId);
  }

  @PreUpdate
  public void preUpdate(final Object entity) {
    if (!(entity instanceof MandantScopedEntity scoped)) {
      return;
    }

    final long ctxMandantId = MandantContext.getRequired();
    validate(scoped, ctxMandantId);
  }

  private void validate(final MandantScopedEntity scoped,
                        final long ctxMandantId) {

    final Long entityMandantId = scoped.getMandant().getId();

    if (entityMandantId == null || entityMandantId != ctxMandantId) {
      throw new IllegalStateException(
              "Mandant mismatch: entity=" + entityMandantId +
                      ", context=" + ctxMandantId
      );
    }
  }
}