package ch.verno.db.jpa;

import ch.verno.db.entity.CourseEntity;
import ch.verno.db.entity.ParticipantEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SpringDataParticipantJpaRepository extends
        JpaRepository<ParticipantEntity, Long>,
        JpaSpecificationExecutor<ParticipantEntity> {

  List<ParticipantEntity> findByCourses(@Nonnull CourseEntity course);

  List<ParticipantEntity> findByCoursesAndActiveTrue(@Nonnull CourseEntity course);

  boolean existsByCourses_Id(@Nonnull Long courseId);

  long countByCourses_Id(@Nonnull Long courseId);
}