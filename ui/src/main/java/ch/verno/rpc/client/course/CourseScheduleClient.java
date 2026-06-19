package ch.verno.rpc.client.course;

import ch.verno.rpc.rpc.RpcFactory;
import ch.verno.contract.endpoint.course.CourseScheduleResource;
import ch.verno.lib.Lazy;
import com.google.inject.Inject;
import jakarta.annotation.Nonnull;

public class CourseScheduleClient {

  @Nonnull private final Lazy<CourseScheduleResource> courseScheduleResource;

  @Inject
  public CourseScheduleClient(@Nonnull final RpcFactory factory) {
    courseScheduleResource = Lazy.of(() -> factory.create(CourseScheduleResource.class));
  }

}
