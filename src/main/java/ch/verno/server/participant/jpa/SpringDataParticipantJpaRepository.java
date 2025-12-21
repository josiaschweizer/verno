package ch.verno.server.participant.jpa;

import ch.verno.server.participant.entity.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataParticipantJpaRepository extends JpaRepository<ParticipantEntity, Long> {
}
