package ch.verno.server.repository.participant;

import ch.verno.db.entity.participant.ParticipantEntity;
import ch.verno.db.jpa.participant.SpringDataParticipantJpaRepository;
import ch.verno.server.repository.base.AbstractEntityRepository;
import jakarta.annotation.Nonnull;

public class ParticipantRepository extends AbstractEntityRepository<
        ParticipantEntity,
        Long,
        SpringDataParticipantJpaRepository> {

  public ParticipantRepository(@Nonnull final SpringDataParticipantJpaRepository repository) {
    super(repository);
  }
}
