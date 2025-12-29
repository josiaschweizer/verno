package ch.verno.db.jpa;

import ch.verno.db.entity.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SpringDataParticipantJpaRepository extends
        JpaRepository<ParticipantEntity, Long>,
        JpaSpecificationExecutor<ParticipantEntity> {
}
