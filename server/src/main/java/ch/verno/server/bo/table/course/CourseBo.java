package ch.verno.server.bo.table.course;

import ch.verno.common.type.CourseScheduleStatus;
import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.intern.table.course.CourseScheduleService;
import ch.verno.server.service.intern.table.course.CourseService;
import jakarta.annotation.Nonnull;

import java.util.List;

public class CourseBo {

  @Nonnull private final Lazy<CourseService> courseService;
  @Nonnull private final Lazy<CourseScheduleService> courseScheduleService;

  CourseBo(@Nonnull final ServerBean serverBean){
    this.courseService = Lazy.of(() -> serverBean.get(CourseService.class));
    this.courseScheduleService = Lazy.of(() -> serverBean.get(CourseScheduleService.class));
  }

  @Nonnull
  public List<CourseDto> getCoursesByCourseScheduleStatus(@Nonnull final CourseScheduleStatus status){
    final var courseSchedules = courseScheduleService.get().findByStatus(status);

    return courseSchedules.stream()
            .flatMap(schedule -> courseService.get().findByCourseScheduleId(schedule.getId()).stream())
            .toList();
  }

}
