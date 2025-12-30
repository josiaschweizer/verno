package ch.verno.db.jpa;

import ch.verno.db.entity.CourseScheduleEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SpringDataCourseScheduleJpaRepository extends
        JpaRepository<CourseScheduleEntity, Long>,
        JpaSpecificationExecutor<CourseScheduleEntity> {

  @Nonnull
  List<CourseScheduleEntity> findByWeeksContains(String week);

}
