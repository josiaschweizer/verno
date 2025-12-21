package ch.verno.persistence.participant;

import ch.verno.domain.participant.ParticipantRepository;
import ch.verno.persistence.participant.entity.ParticipantEntity;
import ch.verno.persistence.participant.jpa.SpringDataParticipantJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
class JpaParticipantRepository implements ParticipantRepository {

  private final SpringDataParticipantJpaRepository jpaRepository;

  JpaParticipantRepository(SpringDataParticipantJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Nonnull
  @Override
  public Optional<ParticipantEntity> findById(@Nonnull final Long id) {
    return jpaRepository.findById(id);
  }

  @Nonnull
  @Override
  public List<ParticipantEntity> findAll() {
    return jpaRepository.findAll();
  }

  @Override
  public void save(@Nonnull final ParticipantEntity participant) {
    jpaRepository.save(participant);
  }
}