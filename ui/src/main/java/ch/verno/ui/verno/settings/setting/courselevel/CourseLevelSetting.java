package ch.verno.ui.verno.settings.setting.courselevel;

import ch.verno.ui.base.settings.VABaseSetting;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
public class CourseLevelSetting extends VABaseSetting {

  @Nonnull
  private final CourseLevelsGrid courseLevelsGrid;

  @Autowired
  public CourseLevelSetting(@Nonnull final CourseLevelsGrid courseLevelsGrid) {
    super("Course Levels");
    this.courseLevelsGrid = courseLevelsGrid;
  }

  @Nonnull
  @Override
  protected Component createContent() {
    return courseLevelsGrid;
  }
}
