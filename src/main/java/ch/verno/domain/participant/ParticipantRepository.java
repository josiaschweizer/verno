package ch.verno.domain.participant;

import ch.verno.persistence.participant.entity.ParticipantEntity;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository {

  @Nonnull
  Optional<ParticipantEntity> findById(@Nonnull final Long id);

  @Nonnull
  List<ParticipantEntity> findAll();

  void save(@Nonnull final ParticipantEntity customer);
}
