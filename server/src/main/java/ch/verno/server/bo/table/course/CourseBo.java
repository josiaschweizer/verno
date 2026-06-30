package ch.verno.server.bo.table.course;

import ch.verno.common.type.CourseScheduleStatus;
import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.course.CourseScheduleDto;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.base.IBusinessObject;
import ch.verno.server.service.entity.course.CourseScheduleService;
import ch.verno.server.service.entity.course.CourseService;
import ch.verno.server.service.entity.participant.ParticipantService;
import jakarta.annotation.Nonnull;

import java.util.List;

public class CourseBo implements IBusinessObject {

  @Nonnull private final Lazy<CourseService> courseService;
  @Nonnull private final Lazy<ParticipantService> participantService;
  @Nonnull private final Lazy<CourseScheduleService> courseScheduleService;

  public CourseBo(@Nonnull final ServerBean serverBean){
    this.courseService = Lazy.of(() -> serverBean.get(CourseService.class));
    this.participantService = Lazy.of(() -> serverBean.get(ParticipantService.class));
    this.courseScheduleService = Lazy.of(() -> serverBean.get(CourseScheduleService.class));
  }

  @Nonnull
  public List<CourseDto> getCoursesByCourseScheduleStatus(@Nonnull final CourseScheduleStatus status){
    final var courseSchedules = courseScheduleService.get().findByStatus(status);

    return courseSchedules.stream()
            .flatMap(schedule -> getCoursesByCourseSchedule(schedule).stream())
            .toList();
  }

  @Nonnull
  public List<CourseDto> getCoursesByCourseSchedule(@Nonnull final CourseScheduleDto courseSchedule) {
    return courseService.get().findByCourseScheduleId(courseSchedule.getId());
  }

  public boolean isCourseReferenced(@Nonnull final CourseDto courseDto) {
    if (courseDto.getId() == null || courseDto.getId() == 0) {
      return false;
    }

    return participantService.get().existsByCourseId(courseDto.getId());
  }

}
