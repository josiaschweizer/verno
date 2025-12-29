package ch.verno.ui.verno.settings;

import ch.verno.server.service.CourseLevelService;
import ch.verno.server.service.MandantSettingService;
import ch.verno.ui.base.settings.VABaseSetting;
import ch.verno.ui.base.settings.VABaseSettingsPage;
import ch.verno.ui.lib.Routes;
import ch.verno.ui.verno.settings.setting.courselevel.CourseLevelSetting;
import ch.verno.ui.verno.settings.setting.quantity.QuantitySetting;
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
  private final CourseLevelSetting courseLevelGridSetting;
  @Nonnull
  private final QuantitySetting quantitySetting;

  @Autowired
  public MandantSettings(@Nonnull final MandantSettingService mandantSettingService,
                         @Nonnull final CourseLevelService courseLevelService) {
    this.courseLevelGridSetting = new CourseLevelSetting(courseLevelService);
    this.quantitySetting = new QuantitySetting(mandantSettingService);

    initUI();
  }

  @Nonnull
  @Override
  protected List<VABaseSetting> createSettings() {
    return List.of(courseLevelGridSetting, quantitySetting);
  }


  @Nonnull
  @Override
  protected String getSettingsPageName() {
    return "Mandant Settings";
  }
}
