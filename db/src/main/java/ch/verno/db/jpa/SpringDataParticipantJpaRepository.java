package ch.verno.db.jpa;

import ch.verno.db.entity.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataParticipantJpaRepository extends JpaRepository<ParticipantEntity, Long> {
}
