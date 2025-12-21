package ch.verno.persistence.participant.jpa;

import ch.verno.persistence.participant.entity.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataParticipantJpaRepository extends JpaRepository<ParticipantEntity, Long> {
}
