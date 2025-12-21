package ch.verno.server.persistence.adapter;

import ch.verno.domain.model.course.Course;
import ch.verno.domain.repository.CourseRepositoryPort;
import ch.verno.server.persistence.entity.CourseEntity;
import ch.verno.server.persistence.mapper.CourseMapper;
import ch.verno.server.persistence.repository.CourseRepository;
import ch.verno.server.persistence.repository.CourseUnitRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CourseJpaAdapter implements CourseRepositoryPort {

  @Nonnull
  private final CourseRepository repo;

  @Nonnull
  private final CourseUnitRepository unitRepo;

  public CourseJpaAdapter(@Nonnull final CourseRepository repo,
                          @Nonnull final CourseUnitRepository unitRepo) {
    this.repo = repo;
    this.unitRepo = unitRepo;
  }

  @Nonnull
  @Override
  public Optional<Course> findById(final long id) {
    return repo.findById(id).map(CourseMapper::toDomain);
  }

  @Nonnull
  @Override
  public Course save(@Nonnull final Course course) {

    final CourseEntity entity;

    if (course.id() == 0L) {
      final var createdAt = course.createdAt();
      entity = CourseEntity.create(createdAt);
    } else {
      entity = repo.findById(course.id())
          .orElseThrow(() -> new IllegalArgumentException("Course not found: " + course.id()));
    }

    entity.setTitle(course.title());
    entity.setCapacity(course.capacity());
    entity.setLocation(course.location());
    entity.setDurationMinutes(course.durationMinutes());
    entity.setCourseLevelId(course.courseLevelId());
    entity.setInstructorId(course.instructorId());

    if (course.courseUnitId() != null) {
      final var unit = unitRepo.findById(course.courseUnitId())
          .orElseThrow(() -> new IllegalArgumentException("CourseUnit not found: " + course.courseUnitId()));
      entity.setCourseUnit(unit);
    } else {
      entity.setCourseUnit(null);
    }

    final var saved = repo.save(entity);
    return CourseMapper.toDomain(saved);
  }

  @Override
  public void deleteById(final long id) {
    repo.deleteById(id);
  }
}