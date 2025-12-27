package ch.verno.ui.verno.settings;

import ch.verno.ui.base.settings.BaseSettingsPage;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;

@Route(Routes.USER_SETTINGS)
@PageTitle("User Settings")
@Menu(order = 97, icon = "vaadin:sliders", title = "User Settings") //todo user user_cog icon from external source
public class UserSettings extends BaseSettingsPage {

  public UserSettings() {

  }

  @Nonnull
  @Override
  protected String getSettingsPageName() {
    return "User Settings";
  }
}
