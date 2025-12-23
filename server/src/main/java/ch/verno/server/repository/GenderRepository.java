package ch.verno.server.repository;

import ch.verno.db.entity.GenderEntity;
import ch.verno.db.jpa.SpringDataGenderJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GenderRepository {

  @Nonnull
  private final SpringDataGenderJpaRepository springDataGenderJpaRepository;

  public GenderRepository(@Nonnull final SpringDataGenderJpaRepository springDataGenderJpaRepository) {
    this.springDataGenderJpaRepository = springDataGenderJpaRepository;
  }

  @Nonnull
  public Optional<GenderEntity> findById(@Nonnull final Long id) {
    return springDataGenderJpaRepository.findById(id);
  }

  @Nonnull
  public List<GenderEntity> findAll() {
    return springDataGenderJpaRepository.findAll();
  }

  public void save(@Nonnull final GenderEntity entity) {
    springDataGenderJpaRepository.save(entity);
  }
}
