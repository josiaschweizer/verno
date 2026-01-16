package ch.verno.ui.verno.settings.setting.courselevel;

import ch.verno.common.db.dto.table.CourseLevelDto;
import ch.verno.common.db.service.ICourseLevelService;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.detail.BaseDetailView;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@UIScope
@SpringComponent
public class CourseLevelDetail extends BaseDetailView<CourseLevelDto> {

  @Nonnull
  private ICourseLevelService courseLevelService;

  public CourseLevelDetail(@Nonnull final ICourseLevelService courseLevelService) {
    this.courseLevelService = courseLevelService;

    init();
  }

  @Override
  protected void onAttach(final AttachEvent attachEvent) {
    // we have to override the attach so that init does not get called every time the detail view gets attached
    // and so the form data always grows (because of the add() calls in init)
  }

  @Autowired
  public void setCourseLevelService(@Nonnull final ICourseLevelService courseLevelService) {
    this.courseLevelService = courseLevelService;
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
    add(createActionButtonLayout());

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

    afterSave.run();
  }

  @Override
  protected void initUI() {
    final var codeEntry = entryFactory.createTextEntry(
            CourseLevelDto::getCode,
            CourseLevelDto::setCode,
            getBinder(),
            Optional.of(getTranslation("setting.code.required")),
            getTranslation("setting.code")
    );
    final var nameEntry = entryFactory.createTextEntry(
            CourseLevelDto::getName,
            CourseLevelDto::setName,
            getBinder(),
            Optional.of(getTranslation("setting.name.required")),
            getTranslation("setting.name")
    );
    final var descriptionEntry = entryFactory.createTextEntry(
            CourseLevelDto::getDescription,
            CourseLevelDto::setDescription,
            getBinder(),
            Optional.empty(),
            getTranslation("setting.description")
    );
    final var sortingOrderEntry = entryFactory.createNumberEntry(
            courseLevelDto -> courseLevelDto.getSortingOrder() != null ? courseLevelDto.getSortingOrder().doubleValue() : 0,
            (courseLevelDto1, sortingOrder) -> courseLevelDto1.setSortingOrder(sortingOrder.intValue()),
            getBinder(),
            Optional.of(getTranslation("setting.sorting.order.required")),
            getTranslation("setting.sorting.order")
    );

    add(createLayoutFromComponents(codeEntry, nameEntry, descriptionEntry, sortingOrderEntry));
  }


  @Nonnull
  @Override
  protected String getDetailPageName() {
    return getTranslation("courseLevel.course_level");
  }

  @Nonnull
  @Override
  protected String getDetailRoute() {
    return Routes.createUrlFromUrlSegments(Routes.COURSE_LEVELS, Routes.DETAIL);
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
