package ch.verno.ui.verno.settings;

import ch.verno.server.service.CourseLevelService;
import ch.verno.server.service.MandantSettingService;
import ch.verno.ui.base.settings.VABaseSetting;
import ch.verno.ui.base.settings.VABaseSettingsPage;
import ch.verno.ui.lib.Routes;
import ch.verno.ui.verno.settings.setting.courselevel.CourseLevelSetting;
import ch.verno.ui.verno.settings.setting.quantity.QuantitySetting;
import ch.verno.ui.verno.settings.setting.shared.SharedSettings;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(Routes.MANDANT_SETTINGS)
@PageTitle("Mandant Settings")
@RolesAllowed({"ADMIN", "MANDANT_ADMIN"})
@Menu(order = 98, icon = "vaadin:cog", title = "Mandant Overview")
public class MandantSettings extends VABaseSettingsPage {

  @Nonnull
  private final QuantitySetting quantitySetting;
  @Nonnull
  private final SharedSettings sharedSetting;
  @Nonnull
  private final CourseLevelSetting courseLevelGridSetting;

  @Autowired
  public MandantSettings(@Nonnull final MandantSettingService mandantSettingService,
                         @Nonnull final CourseLevelService courseLevelService) {
    this.sharedSetting = new SharedSettings(mandantSettingService);
    this.quantitySetting = new QuantitySetting(mandantSettingService);
    this.courseLevelGridSetting = new CourseLevelSetting(courseLevelService);

    initUI();
  }

  @Nonnull
  @Override
  protected List<VABaseSetting<?>> createSettings() {
    return List.of(quantitySetting, sharedSetting, courseLevelGridSetting);
  }


  @Nonnull
  @Override
  protected String getSettingsPageName() {
    return getTranslation("setting.mandant_settings");
  }
}
