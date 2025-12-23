package ch.verno.server.repository;

import ch.verno.db.entity.CourseScheduleEntity;
import ch.verno.db.jpa.SpringDataCourseScheduleJpaRepository;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

public class CourseScheduleRepository {

  @Nonnull
  private final SpringDataCourseScheduleJpaRepository springDataCourseScheduleJpaRepository;

  public CourseScheduleRepository(@Nonnull final SpringDataCourseScheduleJpaRepository springDataCourseScheduleJpaRepository) {
    this.springDataCourseScheduleJpaRepository = springDataCourseScheduleJpaRepository;
  }

  @Nonnull
  public Optional<CourseScheduleEntity> findById(@Nonnull final Long id) {
    return springDataCourseScheduleJpaRepository.findById(id);
  }

  @Nonnull
  public List<CourseScheduleEntity> findAll() {
    return springDataCourseScheduleJpaRepository.findAll();
  }

  public void save(@NonNull final CourseScheduleEntity entity) {
    springDataCourseScheduleJpaRepository.save(entity);
  }
}
