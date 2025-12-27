package ch.verno.ui.verno.settings.setting.courselevel;

import ch.verno.common.db.dto.CourseLevelDto;
import ch.verno.ui.base.settings.VABaseSetting;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringComponent
public class CourseLevelSetting extends VABaseSetting {

  @Nonnull
  private final CourseLevelGrid courseLevelGrid;
  @Nonnull
  private final CourseLevelDetail courseLevelDetail;

  @Autowired
  public CourseLevelSetting(@Nonnull final CourseLevelGrid courseLevelGrid,
                            @Nonnull final CourseLevelDetail courseLevelDetail) {
    super("Course Levels");
    this.courseLevelGrid = courseLevelGrid;
    configureGrid();
    this.courseLevelDetail = courseLevelDetail;
    configureDetailView();

    setActionButton(getAddCourseLevelButton());
  }

  private void configureGrid() {
    this.courseLevelGrid.initUI();
    this.courseLevelGrid.addItemDoubleClickListener(this::onGridItemDoubleClick);
  }

  private void configureDetailView() {
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
