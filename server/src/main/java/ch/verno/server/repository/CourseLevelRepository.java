package ch.verno.server.repository;

import ch.verno.db.entity.CourseLevelEntity;
import ch.verno.db.jpa.SpringDataCourseLevelJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CourseLevelRepository {

  @Nonnull
  private final SpringDataCourseLevelJpaRepository springDataCourseLevelJpaRepository;

  public CourseLevelRepository(@Nonnull final SpringDataCourseLevelJpaRepository springDataCourseLevelJpaRepository) {
    this.springDataCourseLevelJpaRepository = springDataCourseLevelJpaRepository;
  }

  @Nonnull
  public Optional<CourseLevelEntity> findById(final Long id) {
    return springDataCourseLevelJpaRepository.findById(id);
  }

  @Nonnull
  public List<CourseLevelEntity> findAll() {
    return springDataCourseLevelJpaRepository.findAll();
  }

  public CourseLevelEntity save(@Nonnull final CourseLevelEntity courseLevel) {
    return springDataCourseLevelJpaRepository.save(courseLevel);
  }
}
