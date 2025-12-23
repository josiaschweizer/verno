package ch.verno.db.jpa;

import ch.verno.db.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataCourseJpaRepository extends JpaRepository<CourseEntity, Long> {
}
