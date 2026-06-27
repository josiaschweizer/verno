package ch.verno.ui.verno.settings.panels.courselevel;

import ch.verno.common.lib.Routes;
import ch.verno.contract.dto.table.course.CourseLevelDto;
import ch.verno.lib.Lazy;
import ch.verno.rpc.client.course.CourseLevelClient;
import ch.verno.ui.lib.settings.grid.BaseSettingDetail;
import ch.verno.ui.lib.url.RoutesUtil;
import ch.verno.ui.lib.util.LayoutUtil;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.Nonnull;

import java.util.Optional;

@UIScope
@SpringComponent
public class CourseLevelDetail extends BaseSettingDetail<CourseLevelDto> {

  @Nonnull private final Lazy<CourseLevelClient> courseLevelClient;

  @Inject
  public CourseLevelDetail(@Nonnull final Injector injector) {
    super(injector);

    this.courseLevelClient = Lazy.of(() -> injector.getInstance(CourseLevelClient.class));
    init();
  }

  @Override
  protected void initUI() {
    final var codeEntry = entryFactory.createTextField(
            CourseLevelDto::getCode,
            CourseLevelDto::setCode,
            getBinder(),
            Optional.of(getTranslation("setting.code.required")),
            getTranslation("setting.code")
    );
    final var nameEntry = entryFactory.createTextField(
            CourseLevelDto::getName,
            CourseLevelDto::setName,
            getBinder(),
            Optional.of(getTranslation("setting.name.required")),
            getTranslation("setting.name")
    );
    final var descriptionEntry = entryFactory.createTextField(
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

    add(LayoutUtil.createHorizontal(codeEntry, nameEntry, descriptionEntry, sortingOrderEntry));
  }

  @Nonnull
  @Override
  protected String getDetailPageName() {
    return getTranslation("courseLevel.course_level");
  }

  @Nonnull
  @Override
  protected String getDetailRoute() {
    return RoutesUtil.createUrlFromUrlSegments(Routes.COURSE_LEVELS, Routes.DETAIL);
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

  @Override
  protected void createBean(@Nonnull final CourseLevelDto bean) {
    courseLevelClient.get().saveCourseLevel(bean);
  }

  @Override
  protected void updateBean(@Nonnull final CourseLevelDto bean) {
    courseLevelClient.get().saveCourseLevel(bean);
  }

  @Nonnull
  @Override
  protected CourseLevelDto newBeanInstance() {
    return CourseLevelDto.empty();
  }

  @Nonnull
  @Override
  protected Optional<CourseLevelDto> getBeanById(@Nonnull final Long id) {
    return courseLevelClient.get().getCourseLevelById(id);
  }
}