package ch.verno.db.courseschedule;

import ch.verno.server.entity.CourseScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataCourseScheduleJpaRepository extends JpaRepository<CourseScheduleEntity, Long> {
}
