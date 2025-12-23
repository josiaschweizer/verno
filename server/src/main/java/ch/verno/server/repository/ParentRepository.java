package ch.verno.server.repository;

import ch.verno.db.entity.ParentEntity;
import ch.verno.db.jpa.SpringDataParentJpaRepository;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

public class JpaParentRepository {

  @Nonnull
  private final SpringDataParentJpaRepository springDataParentJpaRepository;

  public JpaParentRepository(@Nonnull final SpringDataParentJpaRepository springDataParentJpaRepository) {
    this.springDataParentJpaRepository = springDataParentJpaRepository;
  }

  @Nonnull
  public Optional<ParentEntity> findById(@NonNull final Long id) {
    return springDataParentJpaRepository.findById(id);
  }

  @Nonnull
  public List<ParentEntity> findAll() {
    return springDataParentJpaRepository.findAll();
  }

  public void save(@NonNull final ParentEntity entity) {
    springDataParentJpaRepository.save(entity);
  }
}
