package ch.verno.ui.verno.dashboard.courseSchedules;

import ch.verno.contract.dto.table.course.CourseScheduleDto;
import ch.verno.rpc.client.course.CourseScheduleClient;
import ch.verno.common.type.CourseScheduleStatus;
import ch.verno.contract.dto.filter.CourseScheduleFilter;
import ch.verno.ui.base.components.button.VAButton;
import ch.verno.ui.base.components.dialog.DialogSize;
import ch.verno.ui.base.components.dialog.VAAbstractDialog;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.verno.course.courseschedule.CourseSchedulesGrid;
import com.google.inject.Injector;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.Query;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class CourseScheduleDialog extends VAAbstractDialog {

  @Nonnull private final Injector injector;
  @Nonnull private final CourseScheduleClient courseScheduleClient;

  @Nonnull private final CourseScheduleStatus courseScheduleStatus;
  private final boolean showConfirmDialogOnClose;

  @Nullable private CourseSchedulesGrid grid;
  private VAButton confirmButton;

  public CourseScheduleDialog(@Nonnull final Injector injector,
                              @Nonnull final CourseScheduleClient courseScheduleClient,
                              @Nonnull final CourseScheduleStatus courseScheduleStatus,
                              final boolean showConfirmDialogOnClose) {
    this.injector = injector;
    this.courseScheduleClient = courseScheduleClient;
    this.courseScheduleStatus = courseScheduleStatus;
    this.showConfirmDialogOnClose = showConfirmDialogOnClose;

    String title;
    if (courseScheduleStatus == CourseScheduleStatus.PLANNED) {
      title = getTranslation("courseSchedule.select.course.schedules.to.activate");
    } else {
      title = getTranslation("courseSchedule.select.course.schedules.to.finish");
    }

    initUI(title, DialogSize.BIG);
  }

  @Nonnull
  @Override
  protected HorizontalLayout createContent() {
    grid = new CourseSchedulesGrid(injector, false, false) {

      @Nonnull
      @Override
      protected Stream<CourseScheduleDto> fetch(@Nonnull final Query<CourseScheduleDto, CourseScheduleFilter> query,
                                                @Nonnull final CourseScheduleFilter filter) {
        filter.setStatus(courseScheduleStatus);
        return super.fetch(query, filter);
      }

      @Override
      protected void onGridItemDoubleClick(@Nonnull final ItemDoubleClickEvent<CourseScheduleDto> event) {
        // override it empty to prevent the user to navigate in the background of the dialog
      }
    };
    grid.getGrid().setSelectionMode(Grid.SelectionMode.MULTI);
    grid.getGrid().addSelectionListener(e -> confirmButton.setEnabled(!grid.getGrid().getSelectedItems().isEmpty()));

    final var layout = new HorizontalLayout(grid);
    layout.setHeightFull();
    layout.expand(grid);
    return layout;
  }

  @Nonnull
  @Override
  protected Collection<Button> createActionButtons() {
    return List.of(createCancelButton(), createConfirmButton());
  }

  @Nonnull
  private VAButton createConfirmButton() {
    confirmButton = new VAButton(getTranslation("shared.confirm"), e -> {
      final var selectedItems = grid.getGrid().getSelectedItems();

      if (selectedItems.isEmpty()) {
        NotificationFactory.showInfoNotification(getTranslation("courseSchedule.no.course.schedules.selected.no.changes.were.made"));
      } else if (showConfirmDialogOnClose) {
        final var confirmDialog = new ConfirmDialog(
                getTranslation("shared.confirm.action"),
                getTranslation("shared.this.action.cannot.be.undone.do.you.want.to.continue"),
                getTranslation("shared.confirm"), confirm -> confirmDialog(confirm, selectedItems),
                getTranslation("shared.cancel"),
                cancel -> { /* no action */ }
        );
        confirmDialog.open();
      } else {
        save(selectedItems);
      }

    });
    confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    confirmButton.setEnabled(false);
    return confirmButton;
  }

  private void confirmDialog(@Nonnull final ConfirmDialog.ConfirmEvent confirmListener,
                             @Nonnull final Set<CourseScheduleDto> selectedItems) {
    if (confirmListener.getSource() != null) {
      confirmListener.getSource().close();
      save(selectedItems);
    }
  }

  private void save(@Nonnull final Set<CourseScheduleDto> selectedItems) {
    selectedItems.forEach(course -> {
      if (course.getId() != null) {
        course.setStatus(courseScheduleStatus == CourseScheduleStatus.PLANNED
                ? CourseScheduleStatus.ACTIVE
                : CourseScheduleStatus.COMPLETED
        );

        courseScheduleClient.saveCourseSchedule(course);
      }
    });


    NotificationFactory.showSuccessNotification(getTranslation(getTranslation("courseSchedule.course.schedules.updated.successfully.0.course.schedules.were.updated", selectedItems.size())));
    close();
  }
}
