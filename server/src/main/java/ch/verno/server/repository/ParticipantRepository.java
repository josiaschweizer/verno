package ch.verno.server.repository;

import ch.verno.db.entity.ParticipantEntity;
import ch.verno.db.jpa.SpringDataParticipantJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ParticipantRepository {

  private final SpringDataParticipantJpaRepository jpaRepository;

  ParticipantRepository(@Nonnull final SpringDataParticipantJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Nonnull
  public Optional<ParticipantEntity> findById(@Nonnull final Long id) {
    return jpaRepository.findById(id);
  }

  @Nonnull
  public List<ParticipantEntity> findAll() {
    return jpaRepository.findAll();
  }

  public ParticipantEntity save(@Nonnull final ParticipantEntity participant) {
    return jpaRepository.save(participant);
  }

  public ParticipantEntity update(@Nonnull final ParticipantEntity participant) {
    if (participant.getId() != null) {
      return update(participant.getId(), participant);
    }

    return jpaRepository.save(participant);
  }

  public ParticipantEntity update(@Nonnull final Long id,
                                  @Nonnull final ParticipantEntity participant) {
    if (!jpaRepository.existsById(id)) {
      throw new IllegalStateException("Participant not found: " + id);
    }

    participant.setId(id);
    return jpaRepository.save(participant);
  }
}