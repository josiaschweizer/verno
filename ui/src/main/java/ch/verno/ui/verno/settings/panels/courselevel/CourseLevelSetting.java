package ch.verno.ui.verno.settings.panels.courselevel;

import ch.verno.common.db.dto.table.CourseLevelDto;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.ui.base.settings.grid.BaseGridDetailSetting;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jspecify.annotations.NonNull;

public class CourseLevelSetting extends BaseGridDetailSetting<CourseLevelDto, CourseLevelGrid, CourseLevelDetail> {

  public static final String TITLE_KEY = "courseLevel.course_levels";

  public CourseLevelSetting(@Nonnull final GlobalInterface globalInterface) {
    super(
            globalInterface,
            TITLE_KEY,
            new CourseLevelGrid(globalInterface),
            new CourseLevelDetail(globalInterface)
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


  @NonNull
  @Override
  protected Class<CourseLevelDto> getBeanType() {
    return CourseLevelDto.class;
  }

  @NonNull
  @Override
  protected CourseLevelDto createNewBeanInstance() {
    return new CourseLevelDto();
  }
}