package ch.verno.rpc.client.course;

import ch.verno.common.type.course.courseschedule.status.CourseScheduleStatus;
import ch.verno.contract.dto.filter.CourseFilter;
import ch.verno.contract.dto.response.base.delete.DeleteResponse;
import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.course.CourseScheduleDto;
import ch.verno.contract.endpoint.course.CourseResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.client.helper.SortOrderMapper;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import com.vaadin.flow.data.provider.QuerySortOrder;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public class CourseClient {

  @Nonnull private final Lazy<CourseResource> courseResource;

  @Inject
  public CourseClient(@Nonnull final RpcFactory rpcFactory) {
    this.courseResource = Lazy.of(() -> rpcFactory.create(CourseResource.class));
  }

  @Nonnull
  public Optional<CourseDto> getCourseById(@Nonnull final Long id) {
    return courseResource.get().getCourseById(id);
  }

  @Nonnull
  public List<CourseDto> getCoursesByCourseScheduleStatus(@Nonnull final CourseScheduleStatus status) {
    return courseResource.get().getCoursesByCourseScheduleStatus(status);
  }

  @Nonnull
  public List<CourseDto> getCoursesByCourseSchedule(@Nonnull final CourseScheduleDto courseSchedule) {
    return courseResource.get().getCoursesByCourseSchedule(courseSchedule);
  }

  public boolean isCourseReferenced(@Nonnull final CourseDto courseDto) {
    return courseResource.get().isCourseReferenced(courseDto);
  }

  @Nonnull
  public List<CourseDto> getAllCourses() {
    return courseResource.get().getAllCourses();
  }

  @Nonnull
  public List<CourseDto> getCourses(@Nonnull final CourseFilter courseFilter,
                                    @Nonnull final List<QuerySortOrder> sortOrders,
                                    final int offset,
                                    final int limit) {
    final var orders = SortOrderMapper.toDto(sortOrders);
    return courseResource.get().getCourses(courseFilter, orders, offset, limit);
  }

  @Nonnull
  public CourseDto saveCourse(@Nonnull final CourseDto courseDto) {
    return courseResource.get().saveCourse(courseDto);
  }

  @Nonnull
  public DeleteResponse deleteById(@Nonnull final Long id) {
    return courseResource.get().deleteById(id);
  }

  @Nonnull
  public DeleteResponse delete(@Nonnull final CourseDto dto) {
    return courseResource.get().delete(dto);
  }

}
