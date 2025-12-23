package ch.verno.db.participant;

import ch.verno.server.entity.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataParticipantJpaRepository extends JpaRepository<ParticipantEntity, Long> {
}
