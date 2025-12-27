package ch.verno.ui.verno.course.courseschedule.detail;

import ch.verno.common.db.dto.CourseScheduleDto;
import ch.verno.common.util.VernoConstants;
import ch.verno.server.service.CourseScheduleService;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.detail.BaseDetailView;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.Set;

@PermitAll
@Route(Routes.COURSE_SCHEDULES + Routes.DETAIL)
@PageTitle("Course Schedule Detail")
@Menu(order = 3.21, icon = "vaadin:calendar-envelope", title = "Course Schedule Detail")
public class CourseScheduleDetail extends BaseDetailView<CourseScheduleDto> {

  @Nonnull
  private final CourseScheduleService courseScheduleService;

  public CourseScheduleDetail(@Nonnull final CourseScheduleService courseScheduleService) {
    this.courseScheduleService = courseScheduleService;

    init();
  }

  @Override
  protected void initUI() {
    add(new VerticalLayout(createInfoLayout(), createSchedulePickerLayout()));
  }

  @Nonnull
  private HorizontalLayout createInfoLayout() {
    final var title = entryFactory.createTextEntry(
            CourseScheduleDto::getTitle,
            CourseScheduleDto::setTitle,
            getBinder(),
            Optional.of("Title is required"),
            "Title"
    );
    return createLayoutFromComponents(title);
  }

  @Nonnull
  private HorizontalLayout createSchedulePickerLayout() {
    final var schedulePicker = entryFactory.createScheduleWeekPickerEntry(
            courseScheduleDto -> Set.copyOf(courseScheduleDto.getWeeks()),
            (dto, value) -> dto.setWeeks(value.stream().toList()),
            getBinder(),
            Optional.of("Select at least one week"),
            "Weeks"
    );

    return createLayoutFromComponents(schedulePicker);
  }

  @NonNull
  @Override
  protected String getDetailPageName() {
    return VernoConstants.COURSE_SCHEDULE;
  }

  @NonNull
  @Override
  protected String getDetailRoute() {
    return Routes.createUrlFromUrlSegments(Routes.COURSE_SCHEDULES, Routes.DETAIL);
  }

  @NonNull
  @Override
  protected String getBasePageRoute() {
    return Routes.COURSE_SCHEDULES;
  }

  @NonNull
  @Override
  protected Binder<CourseScheduleDto> createBinder() {
    return new Binder<>(CourseScheduleDto.class);
  }

  @NonNull
  @Override
  protected CourseScheduleDto createBean(@NonNull final CourseScheduleDto bean) {
    return courseScheduleService.createCourseSchedule(bean);
  }

  @NonNull
  @Override
  protected CourseScheduleDto updateBean(@NonNull final CourseScheduleDto bean) {
    return courseScheduleService.updateCourseSchedule(bean);
  }

  @NonNull
  @Override
  protected FormMode getDefaultFormMode() {
    return FormMode.EDIT;
  }

  @NonNull
  @Override
  protected CourseScheduleDto newBeanInstance() {
    return new CourseScheduleDto();
  }

  @Override
  protected CourseScheduleDto getBeanById(@NonNull final Long id) {
    return courseScheduleService.getCourseScheduleById(id);
  }
}
