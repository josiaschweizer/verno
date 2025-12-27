package ch.verno.ui.verno.settings.setting.courselevel;

import ch.verno.server.service.CourseLevelService;
import ch.verno.ui.base.settings.BaseSetting;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nonnull;

public class CourseLevelSetting extends BaseSetting {

  @Nonnull
  private final CourseLevelService courseLevelService;

  public CourseLevelSetting(@Nonnull final CourseLevelService courseLevelService) {
    super("Course Levels");
    this.courseLevelService = courseLevelService;
  }

  @Nonnull
  @Override
  protected Component createContent() {
    return new CourseLevelsGrid(courseLevelService);
  }
}
