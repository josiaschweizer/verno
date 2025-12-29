package ch.verno.db.jpa;

import ch.verno.db.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SpringDataCourseJpaRepository extends
        JpaRepository<CourseEntity, Long>,
        JpaSpecificationExecutor<CourseEntity> {
}
