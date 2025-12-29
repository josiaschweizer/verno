package ch.verno.ui.verno.settings;

import ch.verno.server.service.MandantSettingService;
import ch.verno.ui.base.settings.VABaseSetting;
import ch.verno.ui.base.settings.VABaseSettingsPage;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PermitAll
@Route(Routes.USER_SETTINGS)
@PageTitle("User Settings")
@Menu(order = 97, icon = "vaadin:sliders", title = "User Settings") //todo user user_cog icon from external source
public class UserSettings extends VABaseSettingsPage {

  @Autowired
  public UserSettings(@Nonnull final MandantSettingService mandantSettingService) { //TODO delete course level setting from user setting and replace it with user specific settings (course setting is mandant specific)
    initUI();
  }

  @Nonnull
  @Override
  protected List<VABaseSetting> createSettings() {
    return List.of();
  }

  @Nonnull
  @Override
  protected String getSettingsPageName() {
    return "User Settings";
  }
}
