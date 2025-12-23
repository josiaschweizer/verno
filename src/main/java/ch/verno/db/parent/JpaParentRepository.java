package ch.verno.db.parent;

import ch.verno.server.repository.ParentRepository;
import ch.verno.server.entity.ParentEntity;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

public class JpaParentRepository implements ParentRepository {

  @Nonnull
  private final SpringDataParentJpaRepository springDataParentJpaRepository;

  public JpaParentRepository(@Nonnull final SpringDataParentJpaRepository springDataParentJpaRepository) {
    this.springDataParentJpaRepository = springDataParentJpaRepository;
  }

  @Nonnull
  @Override
  public Optional<ParentEntity> findById(@NonNull final Long id) {
    return springDataParentJpaRepository.findById(id);
  }

  @Nonnull
  @Override
  public List<ParentEntity> findAll() {
    return springDataParentJpaRepository.findAll();
  }

  @Override
  public void save(@NonNull final ParentEntity entity) {
    springDataParentJpaRepository.save(entity);
  }
}
