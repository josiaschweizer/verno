package ch.verno.rpc.client.course;

import ch.verno.common.type.CourseScheduleStatus;
import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.course.CourseScheduleDto;
import ch.verno.contract.endpoint.course.CourseResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
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

  @Nonnull
  public List<CourseDto> getAllCourses() {
    return courseResource.get().getAllCourses();
  }

}
