package ch.verno.ui.verno.dashboard.assignment;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.db.dto.table.CourseDto;
import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.common.db.filter.ParticipantFilter;
import ch.verno.common.db.service.intern.IParticipantService;
import ch.verno.common.db.service.intern.ITenantSettingService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.ui.base.components.button.VAButton;
import ch.verno.ui.base.components.dialog.DialogSize;
import ch.verno.ui.base.components.dialog.VADialog;
import ch.verno.ui.verno.participant.ParticipantsGrid;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.Query;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AssignToCourseDialog extends VADialog {

  @Nonnull private final GlobalInterface globalInterface;
  @Nonnull private final IParticipantService participantService;
  @Nonnull private final ITenantSettingService tenantSettingService;

  @Nonnull private final CourseDto currentCourse;
  @Nonnull private final Set<Long> initiallySelectedParticipantIds;

  @Nullable private ParticipantsGrid grid;

  public AssignToCourseDialog(@Nonnull final GlobalInterface globalInterface,
                              @Nonnull final CourseDto currentCourse) {
    this.globalInterface = globalInterface;
    this.participantService = globalInterface.getService(IParticipantService.class);
    this.tenantSettingService = globalInterface.getService(ITenantSettingService.class);

    this.currentCourse = currentCourse;
    this.initiallySelectedParticipantIds = new HashSet<>();

    initUI(getTranslation("participant.assign.participants"), DialogSize.BIG);
  }

  @Nonnull
  @Override
  protected HorizontalLayout createContent() {
    grid = new ParticipantsGrid(globalInterface, false, false) {

      @Nonnull
      @Override
      protected Stream<ParticipantDto> fetch(@Nonnull final Query<ParticipantDto, ParticipantFilter> query,
                                             @Nonnull final ParticipantFilter filter) {
        filter.setActive(true);
        if (tenantSettingService.getCurrentTenantSettingOrDefault().isEnforceCourseLevelSettings()) {
          filter.setCourseLevelIds(currentCourse.getCourseLevels()
                  .stream()
                  .map(BaseDto::getId)
                  .collect(Collectors.toSet())
          );
        }

        final var items = super.fetch(query, filter).toList();
        if (AssignToCourseDialog.this.grid != null) {
          items.forEach(item -> {
            if (item.getCourses().contains(currentCourse)) {
              initiallySelectedParticipantIds.add(item.getId());
              grid.select(item);
            }
          });
        }

        return items.stream();
      }

      @Override
      public void createContextMenu() {
        // override it empty so we don't have a context menu in this case
      }

      @Override
      protected void onGridItemDoubleClick(@Nonnull final ItemDoubleClickEvent<ParticipantDto> event) {
        final var clickedItem = event.getItem();
        if (grid.getSelectionModel().isSelected(clickedItem)) {
          grid.getSelectionModel().deselect(clickedItem);
        } else {
          grid.getSelectionModel().select(clickedItem);
        }
      }
    };
    grid.getGrid().setSelectionMode(Grid.SelectionMode.MULTI);

    final var layout = new HorizontalLayout(grid);
    layout.setHeightFull();
    layout.expand(grid);
    return layout;
  }

  @Nonnull
  @Override
  protected Collection<Button> createActionButtons() {
    return List.of(createCancelButton(), createSaveButton());
  }

  @Nonnull
  private VAButton createSaveButton() {
    final var button = new VAButton(getTranslation("common.save"), e -> {
      save();
      close();
    });
    button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    return button;
  }

  private void save() {
    if (grid == null) {
      return;
    }

    final var currentlySelectedIds = grid.getGrid()
            .getSelectedItems()
            .stream()
            .map(ParticipantDto::getId)
            .collect(Collectors.toSet());

    final var toAdd = new HashSet<>(currentlySelectedIds);
    toAdd.removeAll(initiallySelectedParticipantIds);

    final var toRemove = new HashSet<>(initiallySelectedParticipantIds);
    toRemove.removeAll(currentlySelectedIds);

    for (final var id : toAdd) {
      participantService.addCourse(id, currentCourse);
    }

    for (final var id : toRemove) {
      participantService.removeCourse(id, currentCourse);
    }
  }

  @Nonnull
  private VAButton createCancelButton() {
    final var button = new VAButton(getTranslation("shared.cancel"));
    button.addClickListener(e -> close());
    return button;
  }
}