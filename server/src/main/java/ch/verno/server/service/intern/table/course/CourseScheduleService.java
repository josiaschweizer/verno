package ch.verno.server.service.intern.table.course;

import ch.verno.common.lib.WeekKey;
import ch.verno.common.type.CourseScheduleStatus;
import ch.verno.contract.dto.table.course.CourseScheduleDto;
import ch.verno.db.entity.course.CourseScheduleEntity;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.course.CourseScheduleMapper;
import ch.verno.server.repository.course.CourseScheduleRepository;
import ch.verno.server.service.base.AbstractEntityServiceLongId;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class CourseScheduleService extends AbstractEntityServiceLongId<
        CourseScheduleEntity,
        CourseScheduleDto,
        CourseScheduleRepository,
        CourseScheduleMapper> {

  public CourseScheduleService(@Nonnull final ServerBean serverBean) {
    super(serverBean.get(CourseScheduleRepository.class), serverBean.get(CourseScheduleMapper.class));
  }

  @Nonnull
  @Transactional(readOnly = true)
  public List<CourseScheduleDto> findByWeek(@Nonnull final LocalDate weekDate) {
    final var week = WeekKey.from(weekDate);
    return getRepository().findByWeek(week)
            .stream()
            .map(getMapper()::toSimpleDto)
            .toList();
  }

  @Nonnull
  @Transactional(readOnly = true)
  public List<CourseScheduleDto> findByStatus(@Nonnull final CourseScheduleStatus status) {
    return getRepository().findByStatus(status)
            .stream()
            .map(getMapper()::toSimpleDto)
            .toList();
  }
}
