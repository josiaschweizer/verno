package ch.verno.ui.verno.settings.setting.courselevel;

import ch.verno.common.db.dto.CourseLevelDto;
import ch.verno.server.service.CourseLevelService;
import ch.verno.ui.base.settings.VABaseSetting;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.icon.VaadinIcon;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class CourseLevelSetting extends VABaseSetting {

  @Nonnull
  private CourseLevelGrid courseLevelGrid;
  @Nonnull
  private CourseLevelDetail courseLevelDetail;

  public CourseLevelSetting(@Nonnull final CourseLevelService courseLevelService) {
    super("Course Levels");
    configureGrid(courseLevelService);
    configureDetailView(courseLevelService);

    setActionButton(getAddCourseLevelButton());
  }

  private void configureGrid(@Nonnull final CourseLevelService courseLevelService) {
    this.courseLevelGrid = new CourseLevelGrid(courseLevelService);
    this.courseLevelGrid.initUI();
    this.courseLevelGrid.addItemDoubleClickListener(this::onGridItemDoubleClick);
  }

  private void configureDetailView(@Nonnull final CourseLevelService courseLevelService) {
    this.courseLevelDetail = new CourseLevelDetail(courseLevelService);
    this.courseLevelDetail.setAfterSave(this::displayCourseLevelGrid);
  }

  @Nonnull
  private Button getAddCourseLevelButton() {
    final var button = new Button("Add Course Level", VaadinIcon.PLUS.create());
    button.addClickListener(clickEvent -> displayCourseLevelDetail(null));
    return button;
  }

  private void displayCourseLevelDetail(@Nullable final Long courseLevelId) {
    setContent(courseLevelDetail);
    setActionButton(createBackToGridButton());
    courseLevelDetail.setParameter(null, courseLevelId);
  }

  @Nonnull
  private Button createBackToGridButton() {
    final var button = new Button("Back to Course Level", VaadinIcon.ARROW_BACKWARD.create());
    button.addClickListener(event -> displayCourseLevelGrid());
    return button;
  }

  private void displayCourseLevelGrid() {
    setContent(courseLevelGrid);
    courseLevelGrid.refresh();
    setActionButton(getAddCourseLevelButton());
  }

  private void onGridItemDoubleClick(@Nonnull final ItemDoubleClickEvent<CourseLevelDto> event) {
    displayCourseLevelDetail(event.getItem().getId());
  }

  @Nonnull
  @Override
  protected Component createContent() {
    return courseLevelGrid;
  }
}
