package ch.verno.ui.verno.dashboard.course;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.common.db.filter.ParticipantFilter;
import ch.verno.common.report.IReportServerGate;
import ch.verno.server.service.CourseLevelService;
import ch.verno.server.service.CourseService;
import ch.verno.server.service.MandantSettingService;
import ch.verno.server.service.ParticipantService;
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
  @Nonnull private final CourseService courseService;
  @Nonnull private final ParticipantService participantService;
  @Nonnull private final CourseLevelService courseLevelService;
  @Nonnull private final MandantSettingService mandantSettingService;
  @Nonnull private final IReportServerGate reportServerGate;

  @Nullable
  private ParticipantsGrid participantsGrid;
  @Nullable
  private List<ParticipantDto> participantsInCurrentCourse;

  public CourseWidget(@Nonnull final Long currentCourseId,
                      @Nonnull final CourseService courseService,
                      @Nonnull final ParticipantService participantService,
                      @Nonnull final CourseLevelService courseLevelService,
                      @Nonnull final MandantSettingService mandantSettingService,
                      @Nonnull final IReportServerGate reportServerGate) {
    this.courseService = courseService;
    this.participantService = participantService;
    this.courseLevelService = courseLevelService;
    this.mandantSettingService = mandantSettingService;
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

    header.add(title, reporButton, titleButton);
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
    this.participantsGrid = new ParticipantsGrid(participantService, courseService, courseLevelService, false, false) {

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
    participantsInCurrentCourse = participantsGrid.getGrid().getSelectedItems().stream().toList();
  }

  @Nonnull
  public String getTitle() {
    return currentCourse.getTitle();
  }
}