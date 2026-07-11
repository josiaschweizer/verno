package ch.verno.server.repository.participant;

import ch.verno.db.entity.course.CourseEntity;
import ch.verno.db.entity.participant.ParticipantEntity;
import ch.verno.db.jpa.participant.SpringDataParticipantJpaRepository;
import ch.verno.server.repository.base.AbstractEntityRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ParticipantRepository extends AbstractEntityRepository<
        ParticipantEntity,
        Long,
        SpringDataParticipantJpaRepository> {

  public ParticipantRepository(@Nonnull final SpringDataParticipantJpaRepository repository) {
    super(repository);
  }

  @Nonnull
  public List<ParticipantEntity> findByCourse(@Nonnull final CourseEntity course) {
    return getRepository().findByCourses(course);
  }

  public boolean existsByCourseId(@Nonnull final Long courseId){
    return getRepository().existsByCourses_Id(courseId);
  }
}
