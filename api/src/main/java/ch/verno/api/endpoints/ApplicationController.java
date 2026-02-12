package ch.verno.api.endpoints;

import ch.verno.api.base.BaseController;
import ch.verno.common.db.enums.CourseScheduleStatus;
import ch.verno.common.db.service.ICourseService;
import ch.verno.common.db.service.IParticipantService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.publ.ApiUrl;
import jakarta.annotation.Nonnull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.APPLICATION)
public class ApplicationController extends BaseController {

  @Nonnull private final ICourseService courseService;
  @Nonnull private final IParticipantService participantService;

  public ApplicationController(@Nonnull final GlobalInterface globalInterface) {
    this.participantService = globalInterface.getService(IParticipantService.class);
    this.courseService = globalInterface.getService(ICourseService.class);
  }

  @GetMapping("memberCount")
  public ResponseEntity<?> getMemberCount() {
    return ok(participantService.getAllParticipants().size());
  }

  @GetMapping("courseCount")
  public ResponseEntity<?> getCoursesCount() {
    return ok(courseService.getAllCourses()
            .stream()
            .filter(item -> {
              final var courseSchedule = item.getCourseSchedule();
              if (courseSchedule == null) {
                return false;
              }

              return courseSchedule.getStatus().equals(CourseScheduleStatus.ACTIVE);
            })
            .toList()
            .size());
  }

}
