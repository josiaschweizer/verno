package ch.verno.db.courseschedule;

import ch.verno.server.repository.CourseScheduleRepository;
import ch.verno.server.entity.CourseScheduleEntity;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

public class JpaCourseScheduleRepository implements CourseScheduleRepository {

  @Nonnull
  private final SpringDataCourseScheduleJpaRepository springDataCourseScheduleJpaRepository;

  public JpaCourseScheduleRepository(@Nonnull final SpringDataCourseScheduleJpaRepository springDataCourseScheduleJpaRepository) {
    this.springDataCourseScheduleJpaRepository = springDataCourseScheduleJpaRepository;
  }

  @Nonnull
  @Override
  public Optional<CourseScheduleEntity> findById(@Nonnull final Long id) {
    return springDataCourseScheduleJpaRepository.findById(id);
  }

  @Nonnull
  @Override
  public List<CourseScheduleEntity> findAll() {
    return springDataCourseScheduleJpaRepository.findAll();
  }

  @Override
  public void save(@NonNull final CourseScheduleEntity entity) {
    springDataCourseScheduleJpaRepository.save(entity);
  }
}
