package ch.verno.ui.verno.settings;

import ch.verno.server.service.CourseLevelService;
import ch.verno.ui.base.settings.BaseSettingsPage;
import ch.verno.ui.lib.Routes;
import ch.verno.ui.verno.settings.setting.courselevel.CourseLevelSetting;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(Routes.USER_SETTINGS)
@PageTitle("User Settings")
@Menu(order = 97, icon = "vaadin:sliders", title = "User Settings") //todo user user_cog icon from external source
public class UserSettings extends BaseSettingsPage {

  @Nonnull
  private final CourseLevelService courseLevelService;

  public UserSettings(@Nonnull final CourseLevelService courseLevelService) {
    this.courseLevelService = courseLevelService;
  }

  @Override
  protected void initUI() {
    final var courseLevelPanel = createCourseLevelPanel();

    add(courseLevelPanel);
  }

  @Nonnull
  private Div createCourseLevelPanel() {
    return new CourseLevelSetting(courseLevelService);
  }

  @Nonnull
  @Override
  protected String getSettingsPageName() {
    return "User Settings";
  }
}
