package ch.verno.db.jpa.participant;

import ch.verno.db.entity.course.CourseEntity;
import ch.verno.db.entity.participant.ParticipantEntity;
import ch.verno.db.jpa.base.AbstractEntityJpaRepository;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public interface SpringDataParticipantJpaRepository extends AbstractEntityJpaRepository<ParticipantEntity, Long> {

  List<ParticipantEntity> findByCourses(@Nonnull CourseEntity course);

  List<ParticipantEntity> findByCoursesAndActiveTrue(@Nonnull CourseEntity course);

  Optional<ParticipantEntity> findByEmail(@Nonnull String email);

  boolean existsByCourses_Id(@Nonnull Long courseId);
}