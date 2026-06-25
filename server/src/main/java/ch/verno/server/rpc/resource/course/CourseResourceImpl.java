package ch.verno.server.rpc.resource.course;

import ch.verno.common.type.CourseScheduleStatus;
import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.endpoint.course.CourseResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.table.course.CourseBo;
import ch.verno.server.service.intern.table.course.CourseService;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
@RpcResource(CourseResource.class)
public class CourseResourceImpl implements CourseResource {

  @Nonnull private final Lazy<CourseBo> courseBo;
  @Nonnull private final Lazy<CourseService> courseService;

  public CourseResourceImpl(@Nonnull final ServerBean serverBean) {
    this.courseBo = Lazy.of(() -> serverBean.get(CourseBo.class));
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
  @Nonnull
  @Override
  public List<CourseDto> getAllCourses() {
    return courseService.get().findAll();
  }

}
