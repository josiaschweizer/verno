package ch.verno.contract.endpoint.course;

import ch.verno.contract.dto.table.course.CourseLevelDto;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

@RpcEndpoint
public interface CourseLevelResource {

  @Nonnull
  Optional<CourseLevelDto> getCourseLevelById(@Nonnull Long id);

  @Nonnull
  Optional<CourseLevelDto> getCourseLevelByCode(@Nonnull String code);

  @Nonnull
  List<CourseLevelDto> getAllCourseLevels();

}
