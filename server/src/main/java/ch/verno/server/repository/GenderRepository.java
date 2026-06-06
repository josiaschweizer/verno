package ch.verno.server.repository;

import ch.verno.db.entity.GenderEntity;
import ch.verno.db.jpa.SpringDataGenderJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GenderRepository {

  @Nonnull private final SpringDataGenderJpaRepository jpaRepository;

  public GenderRepository(@Nonnull final SpringDataGenderJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Nonnull
  public Optional<GenderEntity> findById(@Nonnull final Long id) {
    return jpaRepository.findById(id);
  }

  @Nonnull
  public Optional<GenderEntity> findByName(@Nonnull String name) {
    return jpaRepository.findByName(name);
  }

  @Nonnull
  public List<GenderEntity> findAll() {
    return jpaRepository.findAll();
  }

  @Nonnull
  public GenderEntity save(@Nonnull final GenderEntity entity) {
    return jpaRepository.save(entity);
  }
}
