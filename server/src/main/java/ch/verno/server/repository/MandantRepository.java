package ch.verno.server.repository;

import ch.verno.db.entity.mandant.MandantEntity;
import ch.verno.db.jpa.SpringDataMandantJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MandantRepository {

  @Nonnull private final SpringDataMandantJpaRepository repository;

  public MandantRepository(@Nonnull final SpringDataMandantJpaRepository repository) {
    this.repository = repository;
  }

  @Nonnull
  public List<MandantEntity> findAll() {
    return repository.findAll();
  }

  @Nonnull
  public Optional<MandantEntity> findById(@Nonnull final Long id) {
    return repository.findById(id);
  }

  @Nonnull
  public Optional<MandantEntity> findBySlug(@Nonnull final String slug) {
    return repository.findBySlug(slug);
  }

  @Nonnull
  public Optional<Long> findIdBySlug(String slug) {
    return repository.findIdBySlug(slug);
  }

  @Nonnull
  public MandantEntity save(@Nonnull final MandantEntity entity) {
    return repository.save(entity);
  }

}
