package ch.verno.ui.verno.settings.setting.courselevel;

import ch.verno.ui.base.settings.VABaseSetting;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.spring.annotation.SpringComponent;
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

    setActionButton(getAddCourseLevelButton());
  }

  private Button getAddCourseLevelButton() {
    final var button = new Button("Add Course Level");
    button.addClickListener(this::addCourseLevelButtonClicked);
    return button;
  }

  private void addCourseLevelButtonClicked(@Nonnull final ClickEvent<Button> clickEvent) {

  }

  @Nonnull
  @Override
  protected Component createContent() {
    return courseLevelsGrid;
  }
}
