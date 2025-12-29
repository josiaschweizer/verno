package ch.verno.server.repository;

import ch.verno.db.entity.CourseLevelEntity;
import ch.verno.db.jpa.SpringDataCourseLevelJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CourseLevelRepository {

  @Nonnull
  private final SpringDataCourseLevelJpaRepository jpaRepository;

  public CourseLevelRepository(@Nonnull final SpringDataCourseLevelJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Nonnull
  public Optional<CourseLevelEntity> findById(final Long id) {
    return jpaRepository.findById(id);
  }

  @Nonnull
  public List<CourseLevelEntity> findAll() {
    return jpaRepository.findAll();
  }

  @Nonnull
  public Page<CourseLevelEntity> findAll(@Nonnull final Specification<CourseLevelEntity> spec,
                                    @Nonnull final Pageable pageable) {
    return jpaRepository.findAll(spec, pageable);
  }

  public long count(@Nonnull final Specification<CourseLevelEntity> spec) {
    return jpaRepository.count(spec);
  }

  public CourseLevelEntity save(@Nonnull final CourseLevelEntity courseLevel) {
    return jpaRepository.save(courseLevel);
  }
}
