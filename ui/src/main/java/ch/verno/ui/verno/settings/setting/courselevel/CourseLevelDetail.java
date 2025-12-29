package ch.verno.ui.verno.settings.setting.courselevel;

import ch.verno.common.db.dto.CourseLevelDto;
import ch.verno.common.util.VernoConstants;
import ch.verno.server.service.CourseLevelService;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.detail.BaseDetailView;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@UIScope
@SpringComponent
public class CourseLevelDetail extends BaseDetailView<CourseLevelDto> {

  @Nonnull
  private CourseLevelService courseLevelService;
  @Nullable
  private Runnable afterSave;

  public CourseLevelDetail(@Nonnull final CourseLevelService courseLevelService) {
    this.courseLevelService = courseLevelService;

    init();
  }

  @Autowired
  public void setCourseLevelService(@Nonnull final CourseLevelService courseLevelService) {
    this.courseLevelService = courseLevelService;
  }

  public void setAfterSave(@Nonnull final Runnable afterSave) {
    this.afterSave = afterSave;
  }

  @Override
  protected void init() {
    setWidthFull();
    setHeightFull();
    setPadding(false);
    setSpacing(false);

    initUI();

    this.saveButton.addClickListener(event -> save());
    getBinder().addValueChangeListener(event -> updateSaveButtonState());
    getBinder().addStatusChangeListener(event -> updateSaveButtonState());

    final var spacer = new Div();
    spacer.getStyle().set("flex-grow", "1");

    add(spacer);
    add(createSaveButtonLayout());

    applyFormMode(getDefaultFormMode());
    updateSaveButtonState();
  }

  @Override
  protected void save() {
    if (formMode == FormMode.CREATE) {
      createBean(getBinder().getBean());
    } else if (formMode == FormMode.EDIT) {
      updateBean(getBinder().getBean());
    } else {
      return;
    }

    if (afterSave != null) {
      afterSave.run();
    }
  }

  @Override
  protected void initUI() {
    final var codeEntry = entryFactory.createTextEntry(
            CourseLevelDto::getCode,
            CourseLevelDto::setCode,
            getBinder(),
            Optional.of("Code required"),
            "Code"
    );
    final var nameEntry = entryFactory.createTextEntry(
            CourseLevelDto::getName,
            CourseLevelDto::setName,
            getBinder(),
            Optional.of("Name required"),
            "Name"
    );
    final var descriptionEntry = entryFactory.createTextEntry(
            CourseLevelDto::getDescription,
            CourseLevelDto::setDescription,
            getBinder(),
            Optional.empty(),
            "Description"
    );
    final var sortingOrderEntry = entryFactory.createNumberEntry(
            courseLevelDto -> courseLevelDto.getSortingOrder() != null ? courseLevelDto.getSortingOrder().doubleValue() : 0,
            (courseLevelDto1, sortingOrder) -> courseLevelDto1.setSortingOrder(sortingOrder.intValue()),
            getBinder(),
            Optional.of("Sorting Order required"),
            "Sorting Order"
    );

    add(createLayoutFromComponents(codeEntry, nameEntry, descriptionEntry, sortingOrderEntry));
  }


  @Nonnull
  @Override
  protected String getDetailPageName() {
    return VernoConstants.COURSE_LEVEL;
  }

  @Nonnull
  @Override
  protected String getBasePageRoute() {
    return Routes.COURSE_LEVELS;
  }

  @Nonnull
  @Override
  protected Binder<CourseLevelDto> createBinder() {
    return new Binder<>(CourseLevelDto.class);
  }

  @Nonnull
  @Override
  protected CourseLevelDto createBean(@Nonnull final CourseLevelDto bean) {
    return courseLevelService.createCourseLevel(bean);
  }

  @Nonnull
  @Override
  protected CourseLevelDto updateBean(@Nonnull final CourseLevelDto bean) {
    return courseLevelService.updateCourseLevel(bean);
  }

  @Nonnull
  @Override
  protected FormMode getDefaultFormMode() {
    return FormMode.EDIT;
  }

  @Nonnull
  @Override
  protected CourseLevelDto newBeanInstance() {
    return new CourseLevelDto();
  }

  @Override
  protected CourseLevelDto getBeanById(@Nonnull final Long id) {
    return courseLevelService.getCourseLevelById(id);
  }
}
