package ch.verno.db.courselevel;

import ch.verno.server.repository.CourseLevelRepository;
import ch.verno.server.entity.CourseLevelEntity;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaCourseLevelRepository implements CourseLevelRepository {

  @Nonnull
  private final SpringDataCourseLevelJpaRepository springDataCourseLevelJpaRepository;

  public JpaCourseLevelRepository(@Nonnull final SpringDataCourseLevelJpaRepository springDataCourseLevelJpaRepository) {
    this.springDataCourseLevelJpaRepository = springDataCourseLevelJpaRepository;
  }

  @Override
  public @NonNull Optional<CourseLevelEntity> findById(final Long id) {
    return springDataCourseLevelJpaRepository.findById(id);
  }

  @Override
  public @NonNull List<CourseLevelEntity> findAll() {
    return springDataCourseLevelJpaRepository.findAll();
  }

  @Override
  public void save(@NonNull final CourseLevelEntity courseLevel) {
    springDataCourseLevelJpaRepository.save(courseLevel);
  }
}
