package ch.verno.ui.verno.settings;

import ch.verno.common.lib.Routes;
import ch.verno.ui.lib.icon.CustomIconConstants;
import ch.verno.ui.lib.settings.VABaseSetting;
import ch.verno.ui.lib.settings.VABaseSettingsPage;
import ch.verno.ui.verno.settings.panels.theme.ThemeSetting;
import ch.verno.ui.verno.settings.panels.user.PersonalInformation;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@PermitAll
@Route(Routes.USER_SETTINGS)
@Menu(order = 97, icon = CustomIconConstants.USER_COG, title = "setting.user_settings")
public class UserSettings extends VABaseSettingsPage implements HasDynamicTitle {

  @Inject
  public UserSettings(@Nonnull final Injector injector) {
    super(injector);

    initUI();
  }

  @Nonnull
  @Override
  protected List<VABaseSetting<?>> getSettings() {
    return List.of(
            injector.getInstance(ThemeSetting.class),
            injector.getInstance(PersonalInformation.class)
    );
  }

  @Nonnull
  @Override
  protected String getSettingsPageName() {
    return getTranslation("setting.user_settings");
  }

  @Override
  public String getPageTitle() {
    return getTranslation("setting.user_settings");
  }
}
