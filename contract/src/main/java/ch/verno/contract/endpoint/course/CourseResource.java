package ch.verno.contract.endpoint.course;

import ch.verno.common.type.CourseScheduleStatus;
import ch.verno.contract.dto.filter.CourseFilter;
import ch.verno.contract.dto.response.base.delete.DeleteResponse;
import ch.verno.contract.dto.table.base.SortOrderDto;
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

  boolean isCourseReferenced(@Nonnull CourseDto courseDto);

  @Nonnull
  List<CourseDto> getAllCourses();

  @Nonnull
  List<CourseDto> getCourses(@Nonnull CourseFilter courseFilter,
                             @Nonnull List<SortOrderDto> sortOrders,
                             int offset,
                             int limit);

  @Nonnull
  CourseDto saveCourse(@Nonnull CourseDto courseDto);

  @Nonnull
  DeleteResponse delete(@Nonnull CourseDto courseDto);

  @Nonnull
  DeleteResponse deleteById(@Nonnull Long id);

  @Nonnull
  Long getCourseCount();

}
