package ch.verno.server.rpc.resource.course;

import ch.verno.contract.dto.table.course.CourseLevelDto;
import ch.verno.contract.endpoint.course.CourseLevelResource;
import ch.verno.server.bean.ServerBean;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class CourseLevelResourceImpl implements CourseLevelResource {

  public CourseLevelResourceImpl(@Nonnull final ServerBean serverBean) {
  }

  @Nonnull
  @Override
  public Optional<CourseLevelDto> getCourseLevelByCode(@Nonnull final String code) {
    return Optional.empty();
  }
}
