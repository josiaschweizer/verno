package ch.verno.db.course;

import ch.verno.server.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataCourseJpaRepository extends JpaRepository<CourseEntity, Long> {
}
