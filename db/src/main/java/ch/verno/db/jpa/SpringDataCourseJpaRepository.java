package ch.verno.db.jpa;

import ch.verno.db.entity.CourseEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SpringDataCourseJpaRepository extends
        JpaRepository<CourseEntity, Long>,
        JpaSpecificationExecutor<CourseEntity> {

  @Nonnull
  List<CourseEntity> findByCourseSchedule_Id(Long courseLevelId);

}
