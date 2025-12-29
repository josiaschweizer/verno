package ch.verno.server.repository;

import ch.verno.db.entity.InstructorEntity;
import ch.verno.db.jpa.SpringDataInstructorJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InstructorRepository {

  @Nonnull
  private final SpringDataInstructorJpaRepository jpaRepository;

  public InstructorRepository(@Nonnull final SpringDataInstructorJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Nonnull
  public Optional<InstructorEntity> findById(@Nonnull final Long id) {
    return jpaRepository.findById(id);
  }

  @Nonnull
  public List<InstructorEntity> findAll() {
    return jpaRepository.findAll();
  }

  @Nonnull
  public Page<InstructorEntity> findAll(@Nonnull final Specification<InstructorEntity> spec,
                                        @Nonnull final Pageable pageable) {
    return jpaRepository.findAll(pageable);
  }

  public long count(@Nonnull final Specification<InstructorEntity> spec) {
    return jpaRepository.count(spec);
  }

  @Nonnull
  public InstructorEntity save(@Nonnull final InstructorEntity entity) {
    return jpaRepository.save(entity);
  }
}
