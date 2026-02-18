package ch.verno.ui.verno.settings;

import ch.verno.common.gate.GlobalInterface;
import ch.verno.publ.Routes;
import ch.verno.ui.base.settings.VABaseSetting;
import ch.verno.ui.base.settings.VABaseSettingsPage;
import ch.verno.ui.verno.settings.panels.courselevel.CourseLevelSetting;
import ch.verno.ui.verno.settings.panels.quantity.QuantitySetting;
import ch.verno.ui.verno.settings.panels.report.ReportSetting;
import ch.verno.ui.verno.settings.panels.shared.SharedSettings;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(Routes.TENANT_SETTINGS)
@RolesAllowed({"ADMIN", "MANDANT_ADMIN"})
@Menu(order = 98, icon = "vaadin:cog", title = "setting.tenant_settings")
public class TenantSettings extends VABaseSettingsPage implements HasDynamicTitle {

  @Nonnull private final SharedSettings sharedSetting;
  @Nonnull private final QuantitySetting quantitySetting;
  @Nonnull private final ReportSetting reportSetting;
  @Nonnull private final CourseLevelSetting courseLevelGridSetting;

  @Autowired
  public TenantSettings(@Nonnull final GlobalInterface globalInterface) {
    this.sharedSetting = new SharedSettings(globalInterface);
    this.quantitySetting = new QuantitySetting(globalInterface);
    this.reportSetting = new ReportSetting(globalInterface);
    this.courseLevelGridSetting = new CourseLevelSetting(globalInterface);

    initUI(globalInterface);
  }

  @Nonnull
  @Override
  protected List<VABaseSetting<?>> getSettings() {
    return List.of(quantitySetting, sharedSetting, reportSetting, courseLevelGridSetting);
  }


  @Nonnull
  @Override
  protected String getSettingsPageName() {
    return getTranslation("setting.tenant_settings");
  }

  @Override
  public String getPageTitle() {
    return getTranslation("setting.tenant_settings");
  }
}
