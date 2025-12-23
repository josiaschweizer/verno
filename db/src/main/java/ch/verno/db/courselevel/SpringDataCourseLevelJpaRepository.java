package ch.verno.db.courselevel;

import ch.verno.server.entity.CourseLevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataCourseLevelJpaRepository extends JpaRepository<CourseLevelEntity, Long> {
}
