package ch.verno.ui.verno.settings.setting.courselevel;

import ch.verno.common.db.dto.CourseLevelDto;
import ch.verno.common.db.dto.MandantSettingDto;
import ch.verno.server.service.CourseLevelService;
import ch.verno.ui.base.settings.VABaseSetting;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.binder.Binder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class CourseLevelSetting extends VABaseSetting<MandantSettingDto> {

  @Nonnull
  private final CourseLevelGrid courseLevelGrid;
  @Nonnull
  private final CourseLevelDetail courseLevelDetail;

  public CourseLevelSetting(@Nonnull final CourseLevelService courseLevelService) {
    super("Course Levels");
    setCardDefaultHeight();

    this.courseLevelGrid = createGrid(courseLevelService);
    this.courseLevelDetail = createDetailView(courseLevelService);

    setActionButton(getAddCourseLevelButton());
  }

  private CourseLevelGrid createGrid(@Nonnull final CourseLevelService courseLevelService) {
    final var courseLevelGrid = new CourseLevelGrid(courseLevelService);
    courseLevelGrid.initUI();
    courseLevelGrid.addItemDoubleClickListener(this::onGridItemDoubleClick);
    return courseLevelGrid;
  }

  private CourseLevelDetail createDetailView(@Nonnull final CourseLevelService courseLevelService) {
    final var courseLevelDetail = new CourseLevelDetail(courseLevelService);
    courseLevelDetail.setAfterSave(this::displayCourseLevelGrid);
    return courseLevelDetail;
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

  @Nonnull
  @Override
  protected Binder<MandantSettingDto> createBinder() {
    return new Binder<>(MandantSettingDto.class);
  }

  @Nonnull
  @Override
  protected MandantSettingDto createNewBeanInstance() {
    return new MandantSettingDto();
  }
}
