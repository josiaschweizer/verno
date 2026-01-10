package ch.verno.ui.verno.settings;

import ch.verno.common.db.service.IAppUserService;
import ch.verno.common.db.service.IAppUserSettingService;
import ch.verno.ui.base.settings.VABaseSetting;
import ch.verno.ui.base.settings.VABaseSettingsPage;
import ch.verno.ui.lib.Routes;
import ch.verno.ui.verno.settings.setting.theme.UISetting;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@PermitAll
@Route(Routes.USER_SETTINGS)
@Menu(order = 97, icon = "vaadin:sliders", title = "setting.user_settings")
//todo user user_cog icon from external source
public class UserSettings extends VABaseSettingsPage implements HasDynamicTitle {

  @Nonnull
  private final IAppUserService appUserService;
  @Nonnull
  private final IAppUserSettingService appUserSettingService;

  public UserSettings(@Nonnull final IAppUserService appUserService,
                      @Nonnull final IAppUserSettingService appUserSettingService) {
    this.appUserService = appUserService;
    this.appUserSettingService = appUserSettingService;

    initUI();
  }

  @Nonnull
  @Override
  protected List<VABaseSetting<?>> createSettings() {
    return List.of(new UISetting(appUserService, appUserSettingService));
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
