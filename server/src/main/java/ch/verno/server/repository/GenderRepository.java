package ch.verno.server.repository;

import ch.verno.db.jpa.SpringDataGenderJpaRepository;
import ch.verno.server.repository.GenderRepository;
import ch.verno.server.entity.GenderEntity;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaGenderRepository implements GenderRepository {

  @Nonnull
  private final SpringDataGenderJpaRepository springDataGenderJpaRepository;

  public JpaGenderRepository(@Nonnull final SpringDataGenderJpaRepository springDataGenderJpaRepository) {
    this.springDataGenderJpaRepository = springDataGenderJpaRepository;
  }

  @Nonnull
  @Override
  public Optional<GenderEntity> findById(@Nonnull final Long id) {
    return springDataGenderJpaRepository.findById(id);
  }

  @Nonnull
  @Override
  public List<GenderEntity> findAll() {
    return springDataGenderJpaRepository.findAll();
  }

  @Override
  public void save(@NonNull final GenderEntity entity) {
    springDataGenderJpaRepository.save(entity);
  }
}
