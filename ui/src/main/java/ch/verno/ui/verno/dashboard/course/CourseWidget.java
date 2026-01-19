package ch.verno.ui.verno.dashboard.course;

import ch.verno.common.db.dto.table.CourseDto;
import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.common.db.filter.ParticipantFilter;
import ch.verno.common.db.service.ICourseService;
import ch.verno.common.gate.GlobalGate;
import ch.verno.publ.Publ;
import ch.verno.ui.base.components.widget.VAAccordionWidgetBase;
import ch.verno.ui.verno.dashboard.assignment.AssignToCourseDialog;
import ch.verno.ui.verno.dashboard.report.CourseReportDialog;
import ch.verno.ui.verno.participant.ParticipantsGrid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.Query;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class CourseWidget extends VAAccordionWidgetBase {

  @Nonnull private final CourseDto currentCourse;
  @Nonnull private final GlobalGate globalGate;

  @Nullable private ParticipantsGrid participantsGrid;
  @Nullable private List<ParticipantDto> participantsInCurrentCourse;

  public CourseWidget(@Nonnull final Long currentCourseId,
                      @Nonnull final GlobalGate globalGate) {
    this.globalGate = globalGate;

    final var courseService = globalGate.getService(ICourseService.class);
    this.currentCourse = courseService.getCourseById(currentCourseId);

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
              globalGate,
              currentCourse,
              participantsInCurrentCourse != null ?
                      participantsInCurrentCourse :
                      List.of());
      dialog.open();
    });

    final var assignButton = createHeaderButton(getTranslation("participant.edit.participant"),
            VaadinIcon.COG, e -> {
              final var dialog = new AssignToCourseDialog(
                      globalGate,
                      currentCourse.getId(),
                      participantsInCurrentCourse != null
                              ? participantsInCurrentCourse.stream().map(ParticipantDto::getId).toList()
                              : List.of()
              );

              dialog.addClosedListener(ev -> refresh());
              dialog.addDialogCloseActionListener(ev -> refresh());
              dialog.open();
            });

    final var detailButton = createHeaderButton(Publ.EMPTY_STRING,
            VaadinIcon.EXTERNAL_LINK, e -> {
              final var courseDetailDialog = new CourseDetailDialog(
                      globalGate,
                      currentCourse
              );
              courseDetailDialog.open();
            });

    header.add(reportButton, assignButton, detailButton);
  }

  @Override
  protected void initContent() {
    this.participantsGrid = new ParticipantsGrid(globalGate,
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

  @Override
  protected void refresh() {
    if (participantsGrid == null) {
      return;
    }

    participantsGrid.setFilter(participantsGrid.getFilter());
    participantsInCurrentCourse = participantsGrid.getGrid()
            .getDataProvider()
            .fetch(new Query<>())
            .toList();
  }
}