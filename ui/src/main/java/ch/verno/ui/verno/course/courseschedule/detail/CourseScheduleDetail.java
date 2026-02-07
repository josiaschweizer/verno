package ch.verno.ui.verno.course.courseschedule.detail;

import ch.verno.common.db.dto.table.CourseScheduleDto;
import ch.verno.common.db.enums.CourseScheduleStatus;
import ch.verno.common.db.service.ICourseScheduleService;
import ch.verno.common.db.service.ITenantSettingService;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.pages.detail.BaseDetailView;
import ch.verno.publ.Routes;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.annotation.security.PermitAll;

import java.util.Optional;
import java.util.Set;

@PermitAll
@Route(Routes.COURSE_SCHEDULES + Routes.DETAIL)
@Menu(order = 3.21, icon = "vaadin:calendar-envelope", title = "courseSchedule.course.schedule.detail")
public class CourseScheduleDetail extends BaseDetailView<CourseScheduleDto> implements HasDynamicTitle {

  @Nonnull
  private final ICourseScheduleService courseScheduleService;
  @Nonnull
  private final ITenantSettingService tenantSettingService;

  public CourseScheduleDetail(@Nonnull final ICourseScheduleService courseScheduleService,
                              @Nonnull final ITenantSettingService tenantSettingService) {
    this.courseScheduleService = courseScheduleService;
    this.tenantSettingService = tenantSettingService;

    this.setShowPaddingAroundDetail(true);
  }

  @Override
  protected void initUI() {
    final var setting = tenantSettingService.getCurrentTenantSettingOrDefault();

    add(new VerticalLayout(
            createInfoLayout(),
            createSchedulePickerLayout(
                    setting.getCourseDaysPerSchedule(),
                    setting.isEnforceQuantitySettings()
            )
    ));
  }

  @Nonnull
  private HorizontalLayout createInfoLayout() {
    final var title = entryFactory.createTextEntry(
            CourseScheduleDto::getTitle,
            CourseScheduleDto::setTitle,
            getBinder(),
            Optional.of(getTranslation("shared.title.is.required")),
            getTranslation("shared.title")
    );
    final var status = entryFactory.createEnumComboBoxEntry(
            CourseScheduleDto::getStatus,
            CourseScheduleDto::setStatus,
            getBinder(),
            CourseScheduleStatus.values(),
            Optional.empty(),
            getTranslation(getTranslation("courseSchedule.course.schedule.status")),
            t -> getTranslation(t.getDisplayNameKey())
    );
    final var colorPicker = entryFactory.createColorPickerEntry(
            CourseScheduleDto::getColor,
            CourseScheduleDto::setColor,
            getBinder(),
            Optional.empty(),
            getTranslation("shared.color")
    );

    return createLayoutFromComponents(title, status, colorPicker);
  }

  @Nonnull
  private HorizontalLayout createSchedulePickerLayout(@Nullable final Integer quantityProposalCourseDays,
                                                      final boolean enforceQuantitySetting) {
    final var schedulePicker = entryFactory.createScheduleWeekPickerEntry(
            courseScheduleDto -> Set.copyOf(courseScheduleDto.getWeeks()),
            (dto, value) -> dto.setWeeks(value.stream().toList()),
            getBinder(),
            Optional.of(getTranslation("courseSchedule.select.at.least.one.week")),
            getTranslation("courseSchedule.calendar.week.schedule")
    );
    schedulePicker.setQuantityProposalCourseDays(quantityProposalCourseDays);
    schedulePicker.setEnforceQuantitySetting(enforceQuantitySetting);

    return createLayoutFromComponents(schedulePicker);
  }

  @Nonnull
  @Override
  protected String getDetailPageName() {
    return getTranslation("courseSchedule.course.schedule");
  }

  @Nonnull
  @Override
  protected String getDetailRoute() {
    return Routes.createUrlFromUrlSegments(Routes.COURSE_SCHEDULES, Routes.DETAIL);
  }

  @Nonnull
  @Override
  protected String getBasePageRoute() {
    return Routes.COURSE_SCHEDULES;
  }

  @Nonnull
  @Override
  protected Binder<CourseScheduleDto> createBinder() {
    return new Binder<>(CourseScheduleDto.class);
  }

  @Nonnull
  @Override
  protected CourseScheduleDto createBean(@Nonnull final CourseScheduleDto bean) {
    return courseScheduleService.createCourseSchedule(bean);
  }

  @Nonnull
  @Override
  protected CourseScheduleDto updateBean(@Nonnull final CourseScheduleDto bean) {
    return courseScheduleService.updateCourseSchedule(bean);
  }

  @Nonnull
  @Override
  protected FormMode getDefaultFormMode() {
    return FormMode.EDIT;
  }

  @Nonnull
  @Override
  protected CourseScheduleDto newBeanInstance() {
    return new CourseScheduleDto();
  }

  @Override
  protected CourseScheduleDto getBeanById(@Nonnull final Long id) {
    return courseScheduleService.getCourseScheduleById(id);
  }

  @Override
  public String getPageTitle() {
    return getTranslation("courseSchedule.course.schedule.detail");
  }
}
