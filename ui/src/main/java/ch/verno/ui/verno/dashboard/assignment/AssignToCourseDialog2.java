package ch.verno.ui.verno.dashboard.assignment;

import ch.verno.common.db.dto.table.CourseDto;
import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.ui.base.components.contextmenu.ActionDef;
import ch.verno.ui.base.components.dialog.DialogSize;
import ch.verno.ui.base.components.dialog.VADialog;
import ch.verno.ui.base.pages.grid.ComponentGridColumn;
import ch.verno.ui.base.pages.grid.GridColumnHelper;
import ch.verno.ui.verno.participant.ParticipantsGrid;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import jakarta.annotation.Nonnull;

import java.util.Collection;
import java.util.List;

public class AssignToCourseDialog2 extends VADialog {

  @Nonnull private final GlobalInterface globalInterface;
  @Nonnull private final CourseDto currentCourse;

  @Nonnull private final List<ParticipantDto> selectedParticipants;

  public AssignToCourseDialog2(@Nonnull final GlobalInterface globalInterface,
                               @Nonnull final CourseDto currentCourse,
                               @Nonnull final List<ParticipantDto> selectedParticipants) {
    this.globalInterface = globalInterface;
    this.currentCourse = currentCourse;
    this.selectedParticipants = selectedParticipants;

    initUI("Assign Participants", DialogSize.BIG);
  }


  @Nonnull
  @Override
  protected HorizontalLayout createContent() {
    final var grid = new ParticipantsGrid(globalInterface, false, false) {


      @Override
      public void createContextMenu() {
        // override it empty so we don't have a context menu in this case
      }

      @Nonnull
      @Override
      protected List<ComponentGridColumn<ParticipantDto>> getPrefixComponentColumns() {
        final var columns = super.getPrefixComponentColumns();
        columns.add(GridColumnHelper.componentCol("selectionCheckbox", dto -> createSelectionCheckbox(dto), "Selektiert"));
        return columns;
      }
    };

    return new HorizontalLayout(grid);
  }

  @Nonnull
  private Checkbox createSelectionCheckbox(@Nonnull final ParticipantDto dto) {
    final var checkbox = new Checkbox(false);
    checkbox.setValue(dto.getCourses().contains(currentCourse));
    checkbox.addValueChangeListener(value -> {
      if (value.getValue()) {
        selectedParticipants.add(dto);
      } else {
        selectedParticipants.remove(dto);
      }
    });
    return checkbox;
  }

  @Nonnull
  @Override
  protected Collection<Button> createActionButtons() {
    return List.of();
  }
}
