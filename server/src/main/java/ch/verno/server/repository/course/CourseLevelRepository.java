package ch.verno.server.repository.course;

import ch.verno.db.entity.course.CourseLevelEntity;
import ch.verno.db.jpa.course.SpringDataCourseLevelJpaRepository;
import ch.verno.server.repository.base.AbstractEntityRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CourseLevelRepository extends AbstractEntityRepository<
        CourseLevelEntity,
        Long,
        SpringDataCourseLevelJpaRepository> {

  public CourseLevelRepository(@Nonnull final SpringDataCourseLevelJpaRepository repository) {
    super(repository);
  }

  @Nonnull
  public Optional<CourseLevelEntity> findByCode(@Nonnull String code) {
    return getRepository().findByCode(code);
  }
}
