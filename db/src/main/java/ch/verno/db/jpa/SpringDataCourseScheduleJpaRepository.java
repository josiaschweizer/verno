package ch.verno.db.jpa;

import ch.verno.db.entity.CourseScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SpringDataCourseScheduleJpaRepository extends
        JpaRepository<CourseScheduleEntity, Long>,
        JpaSpecificationExecutor<CourseScheduleEntity> {
}
