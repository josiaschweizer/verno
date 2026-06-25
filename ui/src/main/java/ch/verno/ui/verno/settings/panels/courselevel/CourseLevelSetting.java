package ch.verno.ui.verno.settings.panels.courselevel;

import ch.verno.contract.dto.table.course.CourseLevelDto;
import ch.verno.ui.lib.settings.grid.BaseGridDetailSetting;
import com.google.inject.Injector;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class CourseLevelSetting extends BaseGridDetailSetting<CourseLevelDto, CourseLevelGrid, CourseLevelDetail> {

  public static final String TITLE_KEY = "courseLevel.course_levels";

  public CourseLevelSetting(@Nonnull final Injector injector) {
    super(
            injector,
            TITLE_KEY,
            injector.getInstance(CourseLevelGrid.class),
            injector.getInstance(CourseLevelDetail.class)
    );
  }

  @Nonnull
  @Override
  protected String getAddButtonText() {
    return getTranslation("setting.add.course.level");
  }

  @Nonnull
  @Override
  protected String getBackButtonText() {
    return getTranslation("setting.back.to.course.level");
  }

  @Nullable
  @Override
  protected Long getEntityId(@Nonnull final CourseLevelDto entity) {
    return entity.getId();
  }


  @Nonnull
  @Override
  protected Class<CourseLevelDto> getBeanType() {
    return CourseLevelDto.class;
  }

  @Nonnull
  @Override
  protected CourseLevelDto createNewBeanInstance() {
    return CourseLevelDto.empty();
  }
}