package ch.verno.server.rpc.resource.course;

import ch.verno.common.type.CourseScheduleStatus;
import ch.verno.contract.dto.filter.CourseScheduleFilter;
import ch.verno.contract.dto.table.base.SortOrderDto;
import ch.verno.contract.dto.table.course.CourseScheduleDto;
import ch.verno.contract.endpoint.course.CourseScheduleResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.intern.table.course.CourseScheduleService;
import jakarta.annotation.Nonnull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@RpcResource(CourseScheduleResource.class)
public class CourseScheduleResourceImpl implements CourseScheduleResource {

  @Nonnull private final Lazy<CourseScheduleService> courseScheduleService;

  public CourseScheduleResourceImpl(@Nonnull final ServerBean serverBean) {
    this.courseScheduleService = Lazy.of(() -> serverBean.get(CourseScheduleService.class));
  }

  @Nonnull
  @Override
  public Optional<CourseScheduleDto> getById(@Nonnull final Long id) {
    return courseScheduleService.get().findById(id);
  }

  @Nonnull
  @Override
  public List<CourseScheduleDto> getByWeek(@Nonnull final LocalDate weekDate) {
    return courseScheduleService.get().findByWeek(weekDate);
  }

  @Nonnull
  @Override
  public List<CourseScheduleDto> getByStatus(@Nonnull final CourseScheduleStatus status) {
    return courseScheduleService.get().findByStatus(status);
  }

  @Nonnull
  @Override
  public List<CourseScheduleDto> getCourseSchedules(@Nonnull final CourseScheduleFilter filter,
                                                    final int offset,
                                                    final int limit,
                                                    @Nonnull final List<SortOrderDto> sortOrders) {
    return courseScheduleService.get().findAll(filter, sortOrders, offset, limit);
  }

  @Nonnull
  @Override
  public List<CourseScheduleDto> getCourseSchedules() {
    return courseScheduleService.get().findAll();
  }

  @Nonnull
  @Override
  public CourseScheduleDto saveCourseSchedule(@Nonnull final CourseScheduleDto dto) {
    return  courseScheduleService.get().save(dto);
  }
}
