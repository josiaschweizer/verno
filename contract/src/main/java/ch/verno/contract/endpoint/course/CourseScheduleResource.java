package ch.verno.contract.endpoint.course;

import ch.verno.common.type.CourseScheduleStatus;
import ch.verno.contract.dto.table.course.CourseScheduleDto;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

import java.time.LocalDate;
import java.util.List;

@RpcEndpoint
public interface CourseScheduleResource {

  @Nonnull
  List<CourseScheduleDto> getByWeek(@Nonnull LocalDate weekDate);

  @Nonnull
  List<CourseScheduleDto> getByStatus(@Nonnull CourseScheduleStatus status);

  CourseScheduleDto updateCourseSchedule(@Nonnull CourseScheduleDto dto);

}
