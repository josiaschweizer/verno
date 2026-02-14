package ch.verno.ui.verno.settings.setting.courselevel;

import ch.verno.common.db.dto.table.CourseLevelDto;
import ch.verno.common.db.service.ICourseLevelService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.publ.Routes;
import ch.verno.ui.base.settings.grid.BaseSettingDetail;
import ch.verno.ui.lib.util.LayoutUtil;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.Nonnull;

import java.util.Optional;

@UIScope
@SpringComponent
public class CourseLevelDetail extends BaseSettingDetail<CourseLevelDto> {

  @Nonnull private final ICourseLevelService courseLevelService;

  public CourseLevelDetail(@Nonnull final GlobalInterface globalInterface) {
    super(globalInterface);

    this.courseLevelService = globalInterface.getService(ICourseLevelService.class);
    init();
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

    add(LayoutUtil.createHorizontalLayoutFromComponents(codeEntry, nameEntry, descriptionEntry, sortingOrderEntry));
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
  protected void createBean(@Nonnull final CourseLevelDto bean) {
    courseLevelService.createCourseLevel(bean);
  }

  @Nonnull
  @Override
  protected void updateBean(@Nonnull final CourseLevelDto bean) {
    courseLevelService.updateCourseLevel(bean);
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