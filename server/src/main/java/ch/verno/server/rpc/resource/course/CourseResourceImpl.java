package ch.verno.server.rpc.resource.course;

import ch.verno.common.type.CourseScheduleStatus;
import ch.verno.contract.dto.filter.CourseFilter;
import ch.verno.contract.dto.response.base.delete.DeleteResponse;
import ch.verno.contract.dto.table.base.SortOrderDto;
import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.course.CourseScheduleDto;
import ch.verno.contract.endpoint.course.CourseResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.table.course.CourseBo;
import ch.verno.server.service.entity.course.CourseService;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RpcResource(CourseResource.class)
public class CourseResourceImpl implements CourseResource {

  @Nonnull private final Lazy<CourseBo> courseBo;
  @Nonnull private final Lazy<CourseService> courseService;

  public CourseResourceImpl(@Nonnull final ServerBean serverBean) {
    this.courseBo = Lazy.of(() -> BoFactory.getInstance(serverBean).get(CourseBo.class));
    this.courseService = Lazy.of(() -> serverBean.get(CourseService.class));
  }

  @Nonnull
  @Override
  public Optional<CourseDto> getCourseById(@Nonnull final Long id) {
    return courseService.get().findById(id);
  }

  @Nonnull
  @Override
  public List<CourseDto> getCoursesByCourseScheduleStatus(@Nonnull final CourseScheduleStatus status) {
    return courseBo.get().getCoursesByCourseScheduleStatus(status);
  }

  @Override
  public List<CourseDto> getCoursesByCourseSchedule(@Nonnull final CourseScheduleDto courseSchedule) {
    return courseBo.get().getCoursesByCourseSchedule(courseSchedule);
  }

  @Override
  public boolean isCourseReferenced(@Nonnull final CourseDto courseDto) {
    return courseBo.get().isCourseReferenced(courseDto);
  }

  @Nonnull
  @Override
  public List<CourseDto> getAllCourses() {
    return courseService.get().findAll();
  }

  @Nonnull
  @Override
  public List<CourseDto> getCourses(@Nonnull final CourseFilter courseFilter,
                                    @Nonnull final List<SortOrderDto> sortOrders,
                                    final int offset,
                                    final int limit) {
    return courseService.get().findAll(courseFilter, sortOrders, offset, limit);
  }

  @Nonnull
  @Override
  public CourseDto saveCourse(@Nonnull final CourseDto courseDto) {
    return courseService.get().save(courseDto);
  }

  @Nonnull
  @Override
  public DeleteResponse delete(@Nonnull final CourseDto courseDto) {
    return courseService.get().delete(courseDto);
  }

  @Nonnull
  @Override
  public DeleteResponse deleteById(@Nonnull final Long id) {
    return courseService.get().deleteById(id);
  }

  @Nonnull
  @Override
  public Long getCourseCountUnscoped() {
    return courseService.get().countUnscoped();
  }
}
