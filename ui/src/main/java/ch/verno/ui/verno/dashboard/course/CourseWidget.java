package ch.verno.ui.verno.dashboard.course;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.common.db.filter.ParticipantFilter;
import ch.verno.common.db.service.*;
import ch.verno.common.report.ReportServerGate;
import ch.verno.publ.Publ;
import ch.verno.ui.verno.dashboard.assignment.AssignToCourseDialog;
import ch.verno.ui.verno.dashboard.report.CourseReportDialog;
import ch.verno.ui.verno.participant.ParticipantsGrid;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@CssImport("./components/dashboard/course-widget.css")
public class CourseWidget extends AccordionPanel {

  @Nonnull private final CourseDto currentCourse;
  @Nonnull private final ICourseService courseService;
  @Nonnull private final IInstructorService instructorService;
  @Nonnull private final IParticipantService participantService;
  @Nonnull private final ICourseLevelService courseLevelService;
  @Nonnull private final IMandantSettingService mandantSettingService;
  @Nonnull private final ICourseScheduleService courseScheduleService;
  @Nonnull private final ReportServerGate reportServerGate;

  @Nullable
  private ParticipantsGrid participantsGrid;
  @Nullable
  private List<ParticipantDto> participantsInCurrentCourse;

  public CourseWidget(@Nonnull final Long currentCourseId,
                      @Nonnull final ICourseService courseService,
                      @Nonnull final IInstructorService instructorService,
                      @Nonnull final IParticipantService participantService,
                      @Nonnull final ICourseLevelService courseLevelService,
                      @Nonnull final IMandantSettingService mandantSettingService,
                      @Nonnull final ICourseScheduleService courseScheduleService,
                      @Nonnull final ReportServerGate reportServerGate) {
    this.courseService = courseService;
    this.instructorService = instructorService;
    this.participantService = participantService;
    this.courseLevelService = courseLevelService;
    this.mandantSettingService = mandantSettingService;
    this.courseScheduleService = courseScheduleService;
    this.currentCourse = courseService.getCourseById(currentCourseId);
    this.reportServerGate = reportServerGate;

    setWidthFull();

    initSummary();
    initContent();
  }

  private void initSummary() {
    final var header = new HorizontalLayout();
    header.addClassName("course-widget-header");
    header.setAlignItems(FlexComponent.Alignment.CENTER);
    header.setPadding(false);
    header.setSpacing(true);
    header.setWidthFull();

    final var title = new Span(getTitle());
    title.addClassName(LumoUtility.FontWeight.SEMIBOLD);

    final var reporButton = createHeaderButton("Report", VaadinIcon.FILE_TEXT, event -> {
      final var dialog = new CourseReportDialog(reportServerGate, currentCourse, participantsInCurrentCourse);
      dialog.open();
    });
    final var titleButton = createHeaderButton(getTranslation("participant.edit.participant"), VaadinIcon.COG, event -> {
      final var dialog = new AssignToCourseDialog(
              courseService,
              participantService,
              mandantSettingService,
              currentCourse.getId(),
              participantsInCurrentCourse != null ?
                      participantsInCurrentCourse.stream().map(ParticipantDto::getId).toList() :
                      List.of());

      dialog.addClosedListener(e -> refreshGrid());
      dialog.addDialogCloseActionListener(e -> refreshGrid());

      dialog.open();
    });
    final var courseDetailButton = createHeaderButton(Publ.EMPTY_STRING, VaadinIcon.EXTERNAL_LINK, event -> {
      final var courseDetailDialog = new CourseDetailDialog(
              courseService,
              instructorService,
              courseLevelService,
              courseScheduleService,
              participantService,
              reportServerGate,
              currentCourse
      );
      courseDetailDialog.open();
    });

    header.add(title, reporButton, titleButton, courseDetailButton);
    header.setFlexGrow(1, title);

    setSummary(header);
  }

  @Nonnull
  private Button createHeaderButton(@Nonnull final String text,
                                    @Nonnull final VaadinIcon icon,
                                    @Nonnull final ComponentEventListener<ClickEvent<Button>> listener) {
    final var button = new Button(text, icon.create());
    button.addClassName("course-widget-header__button");

    button.getElement()
            .addEventListener("click", e -> {
            })
            .addEventData("event.stopPropagation()");
    button.addClickListener(listener);
    return button;
  }

  private void initContent() {
    this.participantsGrid = new ParticipantsGrid(participantService,
            courseService,
            courseLevelService,
            reportServerGate,
            false,
            false) {

      @Nonnull
      @Override
      protected Stream<ParticipantDto> fetch(@Nonnull final Query<ParticipantDto, ParticipantFilter> query,
                                             @Nonnull final ParticipantFilter filter) {
        if (currentCourse.getId() != null) {
          filter.setCourseIds(Set.of(currentCourse.getId()));
        }

        final var participants = super.fetch(query, filter).toList();
        CourseWidget.this.participantsInCurrentCourse = participants;
        return participants.stream();
      }
    };

    participantsGrid.getGrid().setAllRowsVisible(true);

    add(participantsGrid);
  }

  private void refreshGrid() {
    if (participantsGrid == null) {
      return;
    }

    participantsGrid.getGrid()
            .getDataProvider()
            .refreshAll();

    participantsInCurrentCourse = participantsGrid.getGrid()
            .getDataProvider()
            .fetch(new Query<>())
            .toList();
  }

  @Nonnull
  public String getTitle() {
    return currentCourse.getTitle();
  }
}