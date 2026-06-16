package ch.verno.ui.verno.course.courses.detail;

import ch.verno.common.db.dto.table.*;
import ch.verno.common.db.filter.ParticipantFilter;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.server.service.intern.ICourseLevelService;
import ch.verno.common.server.service.intern.ICourseScheduleService;
import ch.verno.common.server.service.intern.ICourseService;
import ch.verno.common.server.service.intern.IInstructorService;
import ch.verno.publ.Routes;
import ch.verno.server.service.intern.CourseLevelService;
import ch.verno.server.service.intern.CourseScheduleService;
import ch.verno.server.service.intern.CourseService;
import ch.verno.server.service.intern.InstructorService;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.lib.pages.detail.BaseDetailView;
import ch.verno.ui.lib.util.LayoutUtil;
import ch.verno.ui.verno.participant.ParticipantsGrid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@PermitAll
@Route(Routes.COURSES + Routes.DETAIL)
@Menu(order = 3.11, icon = "vaadin:mobile", title = "course.course.detail")
public class CourseDetail extends BaseDetailView<CourseDto> implements HasDynamicTitle {

  @Nonnull private final GlobalInterface globalInterface;
  @Nonnull private final ICourseService courseService;
  @Nonnull private final IInstructorService instructorService;
  @Nonnull private final ICourseLevelService courseLevelService;
  @Nonnull private final ICourseScheduleService courseScheduleService;


  public CourseDetail(@Nonnull final GlobalInterface globalInterface,
                      final boolean showHeaderToolbar,
                      final boolean showPaddingAroundDetail) {
    this(globalInterface);

    this.setShowHeaderToolbar(showHeaderToolbar);
    this.setShowPaddingAroundDetail(showPaddingAroundDetail);
  }

  @Autowired
  public CourseDetail(@Nonnull final GlobalInterface globalInterface) {
    super(globalInterface);
    this.globalInterface = globalInterface;
    this.courseService = globalInterface.getService(CourseService.class);
    this.instructorService = globalInterface.getService(InstructorService.class);
    this.courseLevelService = globalInterface.getService(CourseLevelService.class);
    this.courseScheduleService = globalInterface.getService(CourseScheduleService.class);

    this.setShowHeaderToolbar(true);
    this.setShowPaddingAroundDetail(true);
  }

  @Nonnull
  @Override
  protected String getDetailPageName() {
    return getTranslation("course.course");
  }

  @Override
  protected void init() {
    setWidthFull();
    setPadding(false);
    setSpacing(false);

    if (showHeaderToolbar && viewToolbar != null) {
      add(viewToolbar.toolbar());
    }

    initUI();

    saveButton.addClickListener(event -> save());
    getBinder().addValueChangeListener(event -> updateSaveButtonState());
    getBinder().addStatusChangeListener(event -> updateSaveButtonState());

    add(createActionButtonLayout());
    initAdditionalInfoUIBelowSaveButton();

    applyFormMode(resolveInitialFormMode());
    updateSaveButtonState();
  }

  @NonNull
  @Override
  protected String getDetailRoute() {
    return Routes.createUrlFromUrlSegments(Routes.COURSES, Routes.DETAIL);
  }

  @Nonnull
  @Override
  protected String getBasePageRoute() {
    return Routes.COURSES;
  }

  @Nonnull
  @Override
  protected Binder<CourseDto> createBinder() {
    return new Binder<>(CourseDto.class);
  }

  @Override
  protected void createBean(@Nonnull final CourseDto bean) {
    courseService.createCourse(bean);
  }

  @Override
  protected void updateBean(@Nonnull final CourseDto bean) {
    courseService.updateCourse(bean);
  }

  @Nonnull
  @Override
  protected FormMode getDefaultFormMode() {
    return FormMode.EDIT;
  }

  @Nonnull
  @Override
  protected CourseDto newBeanInstance() {
    return new CourseDto();
  }

  @Override
  protected CourseDto getBeanById(@Nonnull final Long id) {
    return courseService.getCourseById(id);
  }

  @Override
  protected void initUI() {
    final var infoPanel = createInfoLayout();
    final var coursePanel = createCourseLayout();
    final var notePanel = createNoteLayout();
    final var datePanel = createDateLayout();
    final var verticalLayout = new VerticalLayout(infoPanel, coursePanel, notePanel, datePanel);

    if (!showPaddingAroundDetail) {
      verticalLayout.setPadding(false);
    }

    add(verticalLayout);
  }

  @Nonnull
  private VerticalLayout createInfoLayout() {
    final var titleEntry = entryFactory.createTextField(
            CourseDto::getTitle,
            CourseDto::setTitle,
            getBinder(),
            Optional.of(getTranslation("shared.title.is.required")),
            getTranslation("shared.title"));
    //todo default value from user settings
    final var capacityEntry = entryFactory.createNumberEntry(
            courseDto -> courseDto.getCapacity() != null ? courseDto.getCapacity().doubleValue() : null,
            (dto, value) -> dto.setCapacity(value == null ? null : value.intValue()),
            getBinder(),
            Optional.empty(),
            getTranslation("course.max.capacity"));
    final var location = entryFactory.createTextField(
            CourseDto::getLocation,
            CourseDto::setLocation,
            getBinder(),
            Optional.empty(),
            getTranslation("course.location"));

    final var startTime = entryFactory.createTimeEntry(
            CourseDto::getStartTime,
            CourseDto::setStartTime,
            getBinder(),
            Optional.empty(),
            getTranslation("course.start.time"));
    final var endTime = entryFactory.createTimeEntry(
            CourseDto::getEndTime,
            CourseDto::setEndTime,
            getBinder(),
            Optional.empty(),
            getTranslation("course.end.time"));
    final var color = entryFactory.createColorPickerEntry(
            CourseDto::getColor,
            CourseDto::setColor,
            getBinder(),
            Optional.empty(),
            getTranslation("shared.color")
    );

    final var topLayout = LayoutUtil.createHorizontal(titleEntry, capacityEntry, location);
    final var bottomLayout = LayoutUtil.createHorizontal(startTime, endTime, color);
    return LayoutUtil.createVertical(topLayout, bottomLayout);
  }

  @Nonnull
  private VerticalLayout createCourseLayout() {
    final var courseSchedules = courseScheduleService.getAllCourseSchedules();
    final var courseScheduleOptions = courseSchedules.stream()
            .collect(Collectors.toMap(CourseScheduleDto::getId, CourseScheduleDto::getTitle));

    final var courseScheduleEntry = entryFactory.createComboBoxEntry(
            dto -> dto.getCourseSchedule() != null ? dto.getCourseSchedule().getId() : null,
            (dto, value) -> dto.setCourseSchedule(value == null ?
                    null :
                    courseScheduleService.getCourseScheduleById(value)),
            getBinder(),
            Optional.of(getTranslation("courseSchedule.course.schedule.is.required")),
            getTranslation("courseSchedule.course.schedule"),
            courseScheduleOptions
    );


    final var courseLevelEntry = entryFactory.createMultiSelectComboBoxEntry(
            CourseDto::getCourseLevels,
            CourseDto::setCourseLevels,
            getBinder(),
            Optional.empty(),
            getTranslation("courseLevel.course_levels"),
            courseLevelService.getAllCourseLevels(),
            CourseLevelDto::displayName
    );

    final List<InstructorDto> instructors = instructorService.getAllInstructors().stream()
            .sorted(Comparator.comparing(InstructorDto::displayName))
            .toList();
    final LinkedHashMap<Long, String> instructorOptions = instructors.stream()
            .collect(Collectors.toMap(
                    InstructorDto::getId,
                    InstructorDto::displayName,
                    (a, b) -> a,
                    LinkedHashMap::new
            ));

    final var primaryInstructorEntry = entryFactory.createComboBoxEntry(
            dto -> dto.getInstructor() != null ? dto.getInstructor().getId() : null,
            (dto, value) -> dto.setInstructor(value == null ?
                    null :
                    instructorService.getInstructorById(value)),
            getBinder(),
            Optional.empty(),
            getTranslation("shared.instructor"),
            instructorOptions
    );

    final var secondaryInstructorsEntry = entryFactory.createMultiSelectComboBoxEntry(
            CourseDto::getSecondaryInstructors,
            CourseDto::setSecondaryInstructors,
            getBinder(),
            Optional.empty(),
            getTranslation("shared.zusatzliche.kursleiter"),
            instructors,
            InstructorDto::displayName
    );

    final var topLayout = LayoutUtil.createHorizontal(courseScheduleEntry, courseLevelEntry);
    final var bottomLayout = LayoutUtil.createHorizontal(primaryInstructorEntry, secondaryInstructorsEntry);
    return LayoutUtil.createVertical(topLayout, bottomLayout);
  }

  @Nonnull
  private HorizontalLayout createNoteLayout() {
    final var noteEntry = entryFactory.createTextField(
            CourseDto::getNote,
            CourseDto::setNote,
            getBinder(),
            Optional.empty(),
            getTranslation("shared.note"),
            true
    );

    return LayoutUtil.createHorizontal(noteEntry);
  }

  @Nonnull
  private HorizontalLayout createDateLayout() {
    final var weekOptions = entryFactory.createWeekOptionEntry(
            CourseDto::getWeekdays,
            CourseDto::setWeekdays,
            getBinder(),
            Optional.empty(),
            getTranslation("course.weekdays")
    );

    return LayoutUtil.createHorizontal(weekOptions);
  }

  @Override
  protected void initAdditionalInfoUIBelowSaveButton() {
    final var title = new Span(getTranslation("course.participants.in.this.course"));
    title.getStyle().setFontWeight(Style.FontWeight.BOLD);

    final var participantsGrid = new ParticipantsGrid(globalInterface, false, false) {

      @Nonnull
      @Override
      protected Stream<ParticipantDto> fetch(@Nonnull final Query<ParticipantDto, ParticipantFilter> query,
                                             @Nonnull final ParticipantFilter filter) {
        if (getBinder().getBean() != null && getBinder().getBean().getId() != null) {
          filter.setCourseIds(Set.of(getBinder().getBean().getId()));
        }

        return super.fetch(query, filter);
      }
    };

    participantsGrid.setHeight(null);
    participantsGrid.getGrid().setHeight(null);
    participantsGrid.getGrid().setAllRowsVisible(true);

    addOnLayout = new VerticalLayout(title, participantsGrid);
    addOnLayout.setWidthFull();
    addOnLayout.setHeight(null);

    if (!showPaddingAroundDetail) {
      addOnLayout.setPadding(false);
    }

    add(addOnLayout);
  }

  @Override
  public String getPageTitle() {
    return getTranslation("course.course.detail");
  }
}
