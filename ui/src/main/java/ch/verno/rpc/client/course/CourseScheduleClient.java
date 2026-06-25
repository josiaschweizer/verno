package ch.verno.rpc.client.course;

import ch.verno.common.type.CourseScheduleStatus;
import ch.verno.contract.dto.table.course.CourseScheduleDto;
import ch.verno.contract.endpoint.course.CourseScheduleResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import jakarta.annotation.Nonnull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CourseScheduleClient {

  @Nonnull private final Lazy<CourseScheduleResource> courseScheduleResource;

  @Inject
  public CourseScheduleClient(@Nonnull final RpcFactory factory) {
    courseScheduleResource = Lazy.of(() -> factory.create(CourseScheduleResource.class));
  }

  @Nonnull
  public Optional<CourseScheduleDto> getCourseScheduleById(@Nonnull final Long id) {
    return courseScheduleResource.get().getById(id);
  }

  @Nonnull
  public List<CourseScheduleDto> getCourseScheduleByWeek(@Nonnull final LocalDate weekDate) {
    return courseScheduleResource.get().getByWeek(weekDate);
  }

  @Nonnull
  public List<CourseScheduleDto> getCourseScheduleByStatus(@Nonnull final CourseScheduleStatus status) {
    return courseScheduleResource.get().getByStatus(status);
  }

  @Nonnull
  public CourseScheduleDto saveCourseSchedule(@Nonnull final CourseScheduleDto dto){
    return courseScheduleResource.get().saveCourseSchedule(dto);
  }

}
