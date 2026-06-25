package ch.verno.server.service.intern.table.course;

import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.db.entity.course.CourseEntity;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.db.course.CourseMapper;
import ch.verno.server.repository.course.CourseRepository;
import ch.verno.server.service.base.AbstractEntityService;
import jakarta.annotation.Nonnull;

import java.util.List;

public class CourseService extends AbstractEntityService<
        CourseEntity,
        CourseDto,
        CourseRepository,
        CourseMapper> {

  public CourseService(@Nonnull final ServerBean serverBean) {
    super(serverBean.get(CourseRepository.class), serverBean.get(CourseMapper.class));
  }

  @Nonnull
  public List<CourseDto> findByCourseScheduleId(@Nonnull final Long courseScheduleId) {
    return getRepository().findByCourseScheduleId(courseScheduleId)
            .stream()
            .map(getMapper()::toSimpleDto)
            .toList();
  }

}
