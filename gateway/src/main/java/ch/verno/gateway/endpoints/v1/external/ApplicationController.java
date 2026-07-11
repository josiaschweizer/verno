package ch.verno.gateway.endpoints.v1.external;

import ch.verno.contract.endpoint.course.CourseResource;
import ch.verno.contract.endpoint.participant.ParticipantResource;
import ch.verno.common.lib.api.ApiUrl;
import ch.verno.gateway.base.BaseController;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.APPLICATION)
public class ApplicationController extends BaseController {

  @Nonnull private final Lazy<CourseResource> courseResource;
  @Nonnull private final Lazy<ParticipantResource> participantResource;

  public ApplicationController(@Nonnull final RpcFactory rpcFactory) {
    this.courseResource = Lazy.of(() -> rpcFactory.create(CourseResource.class));
    this.participantResource = Lazy.of(() -> rpcFactory.create(ParticipantResource.class));
  }

  @GetMapping("memberCount")
  public ResponseEntity<?> getMemberCount() { //TODO no tenant for request? annotation?
    return ok(participantResource.get().getParticipantCountUnscoped());
  }

  @GetMapping("courseCount")
  public ResponseEntity<?> getCoursesCount() {
    return ok(courseResource.get().getCourseCountUnscoped()); //TODO course count should be extended with only active and planned courses
  }

}
