package ch.verno.db.jpa;

import ch.verno.db.entity.CourseLevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataCourseLevelJpaRepository extends JpaRepository<CourseLevelEntity, Long> {
}
