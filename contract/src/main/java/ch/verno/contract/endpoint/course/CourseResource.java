package ch.verno.contract.endpoint.course;

import ch.verno.common.type.CourseScheduleStatus;
import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.course.CourseScheduleDto;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

@RpcEndpoint
public interface CourseResource {

  @Nonnull
  Optional<CourseDto> getCourseById(@Nonnull Long id);

  @Nonnull
  List<CourseDto> getCoursesByCourseScheduleStatus(@Nonnull CourseScheduleStatus status);

  List<CourseDto> getCoursesByCourseSchedule(@Nonnull CourseScheduleDto courseSchedule);

  @Nonnull
  List<CourseDto> getAllCourses();


}
