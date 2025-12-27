package ch.verno.server.repository;

import ch.verno.db.entity.ParentEntity;
import ch.verno.db.jpa.SpringDataParentJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ParentRepository {

  @Nonnull
  private final SpringDataParentJpaRepository springDataParentJpaRepository;

  public ParentRepository(@Nonnull final SpringDataParentJpaRepository springDataParentJpaRepository) {
    this.springDataParentJpaRepository = springDataParentJpaRepository;
  }

  @Nonnull
  public Optional<ParentEntity> findById(@Nonnull final Long id) {
    return springDataParentJpaRepository.findById(id);
  }

  @Nonnull
  public List<ParentEntity> findAll() {
    return springDataParentJpaRepository.findAll();
  }

  @Nonnull
  public ParentEntity save(@Nonnull final ParentEntity entity) {
    return springDataParentJpaRepository.save(entity);
  }
}
