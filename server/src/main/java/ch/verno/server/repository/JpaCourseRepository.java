package ch.verno.server.repository;

import ch.verno.db.jpa.SpringDataCourseJpaRepository;
import ch.verno.server.repository.CourseRepository;
import ch.verno.server.entity.CourseEntity;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaCourseRepository implements CourseRepository {

  @Nonnull
  private final SpringDataCourseJpaRepository springDataCourseJpaRepository;

  public JpaCourseRepository(@Nonnull final SpringDataCourseJpaRepository springDataCourseJpaRepository) {
    this.springDataCourseJpaRepository = springDataCourseJpaRepository;
  }

  @Override
  public @NonNull Optional<CourseEntity> findById(final Long id) {
    return springDataCourseJpaRepository.findById(id);
  }

  @Override
  public @NonNull List<CourseEntity> findAll() {
    return springDataCourseJpaRepository.findAll();
  }

  @Override
  public void save(@NonNull final CourseEntity course) {
    springDataCourseJpaRepository.save(course);
  }
}
