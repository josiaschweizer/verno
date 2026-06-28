package ch.verno.server.service.intern.table.participant;

import ch.verno.contract.dto.filter.ParticipantFilter;
import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.db.entity.participant.ParticipantEntity;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.course.CourseMapper;
import ch.verno.server.mapper.participant.ParticipantMapper;
import ch.verno.server.repository.participant.ParticipantRepository;
import ch.verno.server.service.base.AbstractSpecEntityService;
import ch.verno.server.spec.ParticipantSpec;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ParticipantService extends AbstractSpecEntityService<
        Long,
        ParticipantEntity,
        ParticipantDto,
        ParticipantRepository,
        ParticipantMapper,
        ParticipantSpec,
        ParticipantFilter> {

  @Nonnull private final CourseMapper courseMapper;

  public ParticipantService(@Nonnull final ServerBean serverBean) {
    super(serverBean.get(ParticipantRepository.class), serverBean.get(ParticipantMapper.class), ParticipantSpec::new);
    this.courseMapper = serverBean.get(CourseMapper.class);
  }

  @Nonnull
  public List<ParticipantDto> findParticipantsByCourse(@Nonnull final CourseDto course) {
    return getRepository().findByCourse(courseMapper.toEntityReference(course))
            .stream()
            .map(getMapper()::toSimpleDto)
            .toList();
  }

  public boolean existsByCourseId(@Nonnull final Long courseId){
    return getRepository().existsByCourseId(courseId);
  }

}
