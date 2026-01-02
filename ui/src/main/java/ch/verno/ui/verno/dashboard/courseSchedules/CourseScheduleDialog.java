package ch.verno.ui.verno.dashboard.courseSchedules;

import ch.verno.common.db.dto.CourseScheduleDto;
import ch.verno.common.db.enums.CourseScheduleStatus;
import ch.verno.server.service.CourseScheduleService;
import ch.verno.ui.base.components.notification.NotificationFactory;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.dialog.Dialog;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class CourseScheduleDialog extends Dialog {

  @Nonnull
  private final CourseScheduleService courseScheduleService;
  @Nonnull
  private final CourseScheduleStatus status;

  @Nullable
  private CheckboxGroup<CourseScheduleDto> courseScheduleGroup;
  private Button saveButton;

  public CourseScheduleDialog(@Nonnull final CourseScheduleService courseScheduleService,
                              @Nonnull final CourseScheduleStatus status) {
    this.courseScheduleService = courseScheduleService;
    this.status = status;

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

    setHeight("60vh");
    setWidth("min(1500px, 70vw)");
    setMaxWidth("1500px");
    setMinWidth("320px");

    if (status == CourseScheduleStatus.PLANNED) {
      setHeaderTitle(getTranslation("courseSchedule.select.course.schedules.to.activate"));
    } else {
      setHeaderTitle(getTranslation("courseSchedule.select.course.schedules.to.finish"));
    }

    final var cancelButton = new Button(getTranslation("shared.cancel"), event1 -> close());
    saveButton = new Button(getTranslation("common.save"), event -> save());
    saveButton.setEnabled(false);
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    add(courseScheduleGroup);

    getFooter().add(cancelButton);
    getFooter().add(saveButton);
  }

  private void save() {
    if (courseScheduleGroup == null) {
      return;
    }

    final var selectedItems = courseScheduleGroup.getSelectedItems();

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

    if (selectedItems.isEmpty()) {
      NotificationFactory.showInfoNotification(getTranslation("courseSchedule.no.course.schedules.selected.no.changes.were.made"));
    } else {
      NotificationFactory.showSuccessNotification(getTranslation("courseSchedule.course.schedules.updated.successfully"));
    }

    close();
  }
}
