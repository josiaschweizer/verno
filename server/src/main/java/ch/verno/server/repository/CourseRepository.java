package ch.verno.server.repository;

import ch.verno.db.entity.CourseEntity;
import ch.verno.db.jpa.SpringDataCourseJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CourseRepository {

  @Nonnull
  private final SpringDataCourseJpaRepository jpaRepository;

  public CourseRepository(@Nonnull final SpringDataCourseJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Nonnull
  public Optional<CourseEntity> findById(@Nonnull final Long id) {
    return jpaRepository.findById(id);
  }

  @Nonnull
  public List<CourseEntity> findByCourseScheduleId(@Nonnull final Long courseLevelId) {
    return jpaRepository.findByCourseSchedule_Id(courseLevelId);
  }

  @Nonnull
  public List<CourseEntity> findAll() {
    return jpaRepository.findAll();
  }

  public void delete(@Nonnull final Long courseId) {
    jpaRepository.deleteById(courseId);
  }

  public void delete(@Nonnull final CourseEntity course) {
    jpaRepository.delete(course);
  }

  @Nonnull
  public Page<CourseEntity> findAll(@Nonnull final Specification<CourseEntity> spec,
                                    @Nonnull final Pageable pageable) {
    return jpaRepository.findAll(spec, pageable);
  }

  public long count(@Nonnull final Specification<CourseEntity> spec) {
    return jpaRepository.count(spec);
  }

  @Nonnull
  public CourseEntity save(@Nonnull final CourseEntity course) {
    return jpaRepository.save(course);
  }
}
