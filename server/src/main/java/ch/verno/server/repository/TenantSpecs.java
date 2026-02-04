package ch.verno.server.repository;

import ch.verno.db.entity.ParticipantEntity;
import org.springframework.data.jpa.domain.Specification;

final class TenantSpecs {

  private TenantSpecs() {
  }

  static Specification<ParticipantEntity> participantTenant(final long mandantId) {
    return (root, query, cb) -> cb.equal(root.get("mandantId"), mandantId);
    // falls dein Feld anders heisst: z.B. "mandant" (ManyToOne) -> root.get("mandant").get("id")
  }
}