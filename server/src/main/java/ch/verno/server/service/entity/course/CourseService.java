package ch.verno.server.service.entity.course;

import ch.verno.contract.dto.filter.CourseFilter;
import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.db.entity.course.CourseEntity;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.course.CourseMapper;
import ch.verno.server.repository.course.CourseRepository;
import ch.verno.server.service.base.AbstractSpecEntityService;
import ch.verno.server.spec.CourseSpec;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CourseService extends AbstractSpecEntityService<
        Long,
        CourseEntity,
        CourseDto,
        CourseRepository,
        CourseMapper,
        CourseSpec,
        CourseFilter> {

  public CourseService(@Nonnull final ServerBean serverBean) {
    super(serverBean.get(CourseRepository.class), serverBean.get(CourseMapper.class), CourseSpec::new);
  }

  @Nonnull
  public List<CourseDto> findByCourseScheduleId(@Nonnull final Long courseScheduleId) {
    return getRepository().findByCourseScheduleId(courseScheduleId)
            .stream()
            .map(getMapper()::toDto)
            .toList();
  }

  public boolean existsByInstructorId(@Nonnull final Long instructorId) {
    return getRepository().existsByInstructorId(instructorId);
  }

}
