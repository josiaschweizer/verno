package ch.verno.db.jpa.course;

import ch.verno.db.entity.course.CourseLevelEntity;
import ch.verno.db.jpa.base.AbstractEntityJpaRepository;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface SpringDataCourseLevelJpaRepository extends AbstractEntityJpaRepository<CourseLevelEntity, Long> {

  @Nonnull
  Optional<CourseLevelEntity> findByCode(String code);

}
