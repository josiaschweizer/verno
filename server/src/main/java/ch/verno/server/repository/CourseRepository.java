package ch.verno.server.repository;

import ch.verno.db.entity.CourseEntity;
import ch.verno.db.jpa.SpringDataCourseJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CourseRepository {

  @Nonnull
  private final SpringDataCourseJpaRepository springDataCourseJpaRepository;

  public CourseRepository(@Nonnull final SpringDataCourseJpaRepository springDataCourseJpaRepository) {
    this.springDataCourseJpaRepository = springDataCourseJpaRepository;
  }

  @Nonnull
  public Optional<CourseEntity> findById(@Nonnull final Long id) {
    return springDataCourseJpaRepository.findById(id);
  }

  @Nonnull
  public List<CourseEntity> findAll() {
    return springDataCourseJpaRepository.findAll();
  }


  public void save(@Nonnull final CourseEntity course) {
    springDataCourseJpaRepository.save(course);
  }
}
