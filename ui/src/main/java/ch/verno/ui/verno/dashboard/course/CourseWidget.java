package ch.verno.ui.verno.dashboard.course;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.common.db.filter.ParticipantFilter;
import ch.verno.server.service.CourseLevelService;
import ch.verno.server.service.CourseService;
import ch.verno.server.service.MandantSettingService;
import ch.verno.server.service.ParticipantService;
import ch.verno.ui.verno.dashboard.assignment.AssignToCourseDialog;
import ch.verno.ui.verno.participant.ParticipantsGrid;
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

  @Nonnull
  private final CourseDto selectedCourse;
  @Nonnull
  private final CourseService courseService;
  @Nonnull
  private final ParticipantService participantService;
  @Nonnull
  private final CourseLevelService courseLevelService;
  @Nonnull
  private final MandantSettingService mandantSettingService;

  @Nullable
  private ParticipantsGrid participantsGrid;
  @Nullable
  private List<ParticipantDto> selectedParticipants;

  public CourseWidget(@Nonnull final Long selectedCourseId,
                      @Nonnull final CourseService courseService,
                      @Nonnull final ParticipantService participantService,
                      @Nonnull final CourseLevelService courseLevelService,
                      @Nonnull final MandantSettingService mandantSettingService) {
    this.courseService = courseService;
    this.participantService = participantService;
    this.courseLevelService = courseLevelService;
    this.mandantSettingService = mandantSettingService;
    this.selectedCourse = courseService.getCourseById(selectedCourseId);

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

    final var titleButton = new Button(getTranslation("participant.edit.participant"), VaadinIcon.COG.create());
    titleButton.addClassName("course-widget-header__button");

    titleButton.getElement()
            .addEventListener("click", e -> {
            })
            .addEventData("event.stopPropagation()");

    titleButton.addClickListener(event -> {
      final var dialog = new AssignToCourseDialog(
              courseService,
              participantService,
              mandantSettingService,
              selectedCourse.getId(),
              selectedParticipants != null ?
                      selectedParticipants.stream().map(ParticipantDto::getId).toList() :
                      List.of());

      dialog.addClosedListener(e -> refreshGrid());
      dialog.addDialogCloseActionListener(e -> refreshGrid());

      dialog.open();
    });

    header.add(title, titleButton);
    header.setFlexGrow(1, title);

    setSummary(header);
  }

  private void initContent() {
    this.participantsGrid = new ParticipantsGrid(participantService, courseService, courseLevelService, false, false) {

      @Nonnull
      @Override
      protected Stream<ParticipantDto> fetch(@Nonnull final Query<ParticipantDto, ParticipantFilter> query,
                                             @Nonnull final ParticipantFilter filter) {
        if (selectedCourse.getId() != null) {
          filter.setCourseIds(Set.of(selectedCourse.getId()));
        }

        final var participants = super.fetch(query, filter).toList();
        CourseWidget.this.selectedParticipants = participants;
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
  }

  @Nonnull
  public String getTitle() {
    return selectedCourse.getTitle();
  }
}