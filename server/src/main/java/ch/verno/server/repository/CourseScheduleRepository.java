package ch.verno.server.repository;

import ch.verno.common.db.enums.CourseScheduleStatus;
import ch.verno.db.entity.CourseScheduleEntity;
import ch.verno.db.jpa.SpringDataCourseScheduleJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CourseScheduleRepository {

  @Nonnull
  private final SpringDataCourseScheduleJpaRepository jpaRepository;

  public CourseScheduleRepository(@Nonnull final SpringDataCourseScheduleJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Nonnull
  public Optional<CourseScheduleEntity> findById(@Nonnull final Long id) {
    return jpaRepository.findById(id);
  }

  @Nonnull
  public List<CourseScheduleEntity> findByWeek(@Nonnull final String week) {
    return jpaRepository.findByWeeksContains(week);
  }

  @Nonnull
  public List<CourseScheduleEntity> findByStatus(@Nonnull final CourseScheduleStatus status) {
    return jpaRepository.findByStatus(status);
  }

  @Nonnull
  public List<CourseScheduleEntity> findAll() {
    return jpaRepository.findAll();
  }

  @Nonnull
  public Page<CourseScheduleEntity> findAll(@Nonnull final Specification<CourseScheduleEntity> spec,
                                            @Nonnull final Pageable pageable) {
    return jpaRepository.findAll(spec, pageable);
  }

  public long count(@Nonnull final Specification<CourseScheduleEntity> spec) {
    return jpaRepository.count(spec);
  }

  @Nonnull
  public CourseScheduleEntity save(@Nonnull final CourseScheduleEntity entity) {
    return jpaRepository.save(entity);
  }
}
