package ch.verno.rpc.client.course;

import ch.verno.contract.dto.table.course.CourseLevelDto;
import ch.verno.contract.endpoint.course.CourseLevelResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class CourseLevelClient {

  @Nonnull private final Lazy<CourseLevelResource> courseLevelResource;

  @Inject
  public CourseLevelClient(@Nonnull final RpcFactory rpcFactory) {
    this.courseLevelResource = Lazy.of(() -> rpcFactory.create(CourseLevelResource.class));
  }

  @Nonnull
  public Optional<CourseLevelDto> getCourseLevelByCode(@Nonnull final String code) {
    return courseLevelResource.get().getCourseLevelByCode(code);
  }

}
