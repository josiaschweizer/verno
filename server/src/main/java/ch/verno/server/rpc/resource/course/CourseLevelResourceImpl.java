package ch.verno.server.rpc.resource.course;

import ch.verno.contract.dto.table.course.CourseLevelDto;
import ch.verno.contract.endpoint.course.CourseLevelResource;
import ch.verno.contract.endpoint.course.CourseResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.intern.table.course.CourseLevelService;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@RpcResource(CourseResource.class)
public class CourseLevelResourceImpl implements CourseLevelResource {

  @Nonnull private final Lazy<CourseLevelService> courseLevelService;

  public CourseLevelResourceImpl(@Nonnull final ServerBean serverBean) {
    this.courseLevelService = Lazy.of(() -> serverBean.get(CourseLevelService.class));
  }

  @Nonnull
  @Override
  public Optional<CourseLevelDto> getCourseLevelById(@Nonnull final Long id) {
    return courseLevelService.get().findById(id);
  }

  @Nonnull
  @Override
  public Optional<CourseLevelDto> getCourseLevelByCode(@Nonnull final String code) {
    return courseLevelService.get().findByCode(code);
  }

  @Nonnull
  @Override
  public List<CourseLevelDto> getAllCourseLevels() {
    return courseLevelService.get().findAll();
  }
}
