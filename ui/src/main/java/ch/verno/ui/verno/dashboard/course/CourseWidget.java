package ch.verno.ui.verno.dashboard.course;

import ch.verno.common.db.dto.table.CourseDto;
import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.common.db.filter.ParticipantFilter;
import ch.verno.common.db.service.ICourseService;
import ch.verno.common.db.service.IParticipantService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.publ.Publ;
import ch.verno.ui.base.components.widget.VAAccordionWidgetBase;
import ch.verno.ui.verno.dashboard.assignment.AssignToCourseDialog;
import ch.verno.ui.verno.dashboard.report.CourseReportDialog;
import ch.verno.ui.verno.participant.ParticipantsGrid;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.Query;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class CourseWidget extends VAAccordionWidgetBase {

  @Nonnull private final GlobalInterface globalInterface;

  @Nonnull private final CourseDto currentCourse;
  private final IParticipantService participantService;

  @Nullable private ParticipantsGrid participantsGrid;
  @Nonnull private List<ParticipantDto> participantsInCourse;

  public CourseWidget(@Nonnull final Long currentCourseId,
                      @Nonnull final GlobalInterface globalInterface) {
    this.globalInterface = globalInterface;

    final var courseService = globalInterface.getService(ICourseService.class);
    currentCourse = courseService.getCourseById(currentCourseId);

    participantService = globalInterface.getService(IParticipantService.class);
    participantsInCourse = participantService.findParticipants(
            ParticipantFilter.fromCourseId(currentCourse.getId() != null ? Set.of(currentCourse.getId()) : null)
    );

    build();
  }

  @Nonnull
  @Override
  protected String getTitleText() {
    return currentCourse.getTitle();
  }

  @Override
  protected void buildHeaderActions(@Nonnull final HorizontalLayout header) {
    final var reportButton = createHeaderButton("Report", VaadinIcon.FILE_TEXT, e -> {
      final var dialog = new CourseReportDialog(
              globalInterface,
              currentCourse,
              participantsInCourse);
      dialog.open();
    });

    final var assignButton = createHeaderButton(getTranslation("participant.edit.participant"),
            VaadinIcon.COG, e -> {
              final var dialog = new AssignToCourseDialog(
                      globalInterface,
                      currentCourse.getId(),
                      participantsInCourse.stream().map(ParticipantDto::getId).toList()
              );

              dialog.addClosedListener(ev -> refresh());
              dialog.addDialogCloseActionListener(ev -> refresh());
              dialog.open();
            });

    final var detailButton = createHeaderButton(Publ.EMPTY_STRING,
            VaadinIcon.EXTERNAL_LINK, e -> {
              final var courseDetailDialog = new CourseDetailDialog(
                      globalInterface,
                      currentCourse
              );
              courseDetailDialog.open();
            });

    header.add(reportButton, assignButton, detailButton);
  }

  @Override
  protected void initContent() {
    if (!participantsInCourse.isEmpty()) {
      participantsGrid = new ParticipantsGrid(globalInterface,
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
          CourseWidget.this.participantsInCourse = participants;
          return participants.stream();
        }
      };

      participantsGrid.getGrid().setAllRowsVisible(true);
      add(participantsGrid);
    } else {
      add(new Text(getTranslation("shared.no.participants.assigned.to.this.course.yet")));
    }
  }

  @Override
  protected void refresh() {
    if (participantsGrid == null) {
      participantsInCourse = participantService.findParticipants(ParticipantFilter.fromCourseId(currentCourse.getId() != null ? Set.of(currentCourse.getId()) : null));
      return;
    }

    participantsGrid.setFilter(participantsGrid.getFilter());
    participantsInCourse = participantsGrid.getGrid()
            .getDataProvider()
            .fetch(new Query<>())
            .toList();
  }
}