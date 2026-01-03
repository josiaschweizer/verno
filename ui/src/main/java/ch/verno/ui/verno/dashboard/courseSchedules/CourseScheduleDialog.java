package ch.verno.ui.verno.dashboard.courseSchedules;

import ch.verno.common.db.dto.CourseScheduleDto;
import ch.verno.common.db.enums.CourseScheduleStatus;
import ch.verno.server.service.CourseScheduleService;
import ch.verno.ui.base.components.notification.NotificationFactory;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Set;

public class CourseScheduleDialog extends Dialog {

  @Nonnull
  private final CourseScheduleService courseScheduleService;
  @Nonnull
  private final CourseScheduleStatus status;

  private final boolean showConfirmDialogOnClose;

  @Nullable
  private CheckboxGroup<CourseScheduleDto> courseScheduleGroup;
  @Nullable
  private Button saveButton;

  public CourseScheduleDialog(@Nonnull final CourseScheduleService courseScheduleService,
                              @Nonnull final CourseScheduleStatus status,
                              final boolean showConfirmDialogOnClose) {
    this.courseScheduleService = courseScheduleService;
    this.status = status;
    this.showConfirmDialogOnClose = showConfirmDialogOnClose;

    initUI();
  }

  private void initUI() {
    final var courseSchedules = courseScheduleService.getCourseSchedulesByStatus(status);
    courseScheduleGroup = new CheckboxGroup<>();
    courseScheduleGroup.setItems(courseSchedules);
    courseScheduleGroup.setItemLabelGenerator(CourseScheduleDto::displayName);
    courseScheduleGroup.addValueChangeListener(e -> {
      saveButton.setEnabled(!e.getValue().isEmpty());
    });

    setHeight("40vh");
    setWidth("min(1000px, 50vw)");
    setMaxWidth("1000px");
    setMinWidth("320px");

    if (status == CourseScheduleStatus.PLANNED) {
      setHeaderTitle(getTranslation("courseSchedule.select.course.schedules.to.activate"));
    } else {
      setHeaderTitle(getTranslation("courseSchedule.select.course.schedules.to.finish"));
    }

    final var cancelButton = new Button(getTranslation("shared.cancel"), event1 -> close());
    saveButton = new Button(getTranslation("common.save"), event -> preSave());
    saveButton.setEnabled(false);
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    add(courseScheduleGroup);

    getFooter().add(cancelButton);
    getFooter().add(saveButton);
  }

  private void preSave() {
    if (courseScheduleGroup == null) {
      return;
    }

    final var selectedItems = courseScheduleGroup.getSelectedItems();

    if (selectedItems.isEmpty()) {
      NotificationFactory.showInfoNotification(getTranslation("courseSchedule.no.course.schedules.selected.no.changes.were.made"));
      return;
    } else if (showConfirmDialogOnClose) {
      final var confirmDialog = new ConfirmDialog(
              "Confirm action",
              "This action cannot be undone. Do you want to continue?",
              "Confirm", confirm -> confirmDialog(confirm, selectedItems),
              "Cancel",
              cancel -> { /* no action */ }
      );
      confirmDialog.open();
    } else {
      save(selectedItems);
    }
  }

  private void save(@Nonnull final Set<CourseScheduleDto> selectedItems) {
    selectedItems.forEach(course -> {
      if (course.getId() != null) {
        course.setStatus(
                status == CourseScheduleStatus.PLANNED
                        ? CourseScheduleStatus.ACTIVE
                        : CourseScheduleStatus.COMPLETED
        );
        courseScheduleService.updateCourseSchedule(course);
      }
    });


    NotificationFactory.showSuccessNotification(getTranslation(getTranslation("courseSchedule.course.schedules.updated.successfully.0.course.schedules.were.updated", selectedItems.size())));
    close();
  }

  private void confirmDialog(@Nonnull final ConfirmDialog.ConfirmEvent confirmListener,
                             @Nonnull final Set<CourseScheduleDto> selectedItems) {
    if (confirmListener.getSource() != null) {
      confirmListener.getSource().close();
      save(selectedItems);
    }
  }

}
