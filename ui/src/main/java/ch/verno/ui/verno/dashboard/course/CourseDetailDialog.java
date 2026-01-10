package ch.verno.ui.verno.dashboard.course;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.service.*;
import ch.verno.ui.verno.course.courses.detail.CourseDetail;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

public class CourseDetailDialog extends Dialog {

  @Nonnull private final ICourseService courseService;
  @Nonnull private final IInstructorService instructorService;
  @Nonnull private final ICourseLevelService courseLevelService;
  @Nonnull private final ICourseScheduleService courseScheduleService;
  @Nonnull private final IParticipantService participantService;
  @Nonnull private final CourseDto currentCourse;

  public CourseDetailDialog(@Nonnull final ICourseService courseService,
                            @Nonnull final IInstructorService instructorService,
                            @Nonnull final ICourseLevelService courseLevelService,
                            @Nonnull final ICourseScheduleService courseScheduleService,
                            @Nonnull final IParticipantService participantService,
                            @Nonnull final CourseDto currentCourse) {
    this.courseService = courseService;
    this.instructorService = instructorService;
    this.courseLevelService = courseLevelService;
    this.courseScheduleService = courseScheduleService;
    this.participantService = participantService;
    this.currentCourse = currentCourse;

    initUI();
  }

  private void initUI() {
    final var content = createCourseDetail();

    setHeight("90vh");
    setWidth("min(1500px, 95vw)");
    setMaxWidth("1500px");
    setMinWidth("320px");

    setHeaderTitle("Course Detail");
    add(content);
  }

  @Nonnull
  private VerticalLayout createCourseDetail() {
    final var courseDetail = new CourseDetail(
            courseService,
            instructorService,
            courseLevelService,
            courseScheduleService,
            participantService,
            false,
            false);
    courseDetail.setParameter(null, currentCourse.getId());
    courseDetail.setAfterSave(this::close);
    courseDetail.setPadding(false);
    courseDetail.setMargin(false);
    return courseDetail;
  }

}
