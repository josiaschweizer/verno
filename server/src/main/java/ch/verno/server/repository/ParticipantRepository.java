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

  public void save(@Nonnull final ParticipantEntity participant) {
    jpaRepository.save(participant);
  }
}