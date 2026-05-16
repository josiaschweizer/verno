package ch.verno.db.jpa;

import ch.verno.db.entity.CourseLevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface SpringDataCourseLevelJpaRepository extends
        JpaRepository<CourseLevelEntity, Long>,
        JpaSpecificationExecutor<CourseLevelEntity> {

  @Nonnull
  Optional<CourseLevelEntity> findByCode(String code);

}
