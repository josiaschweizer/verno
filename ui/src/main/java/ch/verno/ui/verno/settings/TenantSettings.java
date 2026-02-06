package ch.verno.ui.verno.settings;

import ch.verno.common.db.service.ICourseLevelService;
import ch.verno.common.db.service.ITenantSettingService;
import ch.verno.common.lib.Routes;
import ch.verno.ui.base.settings.VABaseSetting;
import ch.verno.ui.base.settings.VABaseSettingsPage;
import ch.verno.ui.verno.settings.setting.courselevel.CourseLevelSetting;
import ch.verno.ui.verno.settings.setting.quantity.QuantitySetting;
import ch.verno.ui.verno.settings.setting.report.ReportSetting;
import ch.verno.ui.verno.settings.setting.shared.SharedSettings;
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
  public TenantSettings(@Nonnull final ITenantSettingService tenantSettingService,
                        @Nonnull final ICourseLevelService courseLevelService) {
    this.sharedSetting = new SharedSettings(tenantSettingService);
    this.quantitySetting = new QuantitySetting(tenantSettingService);
    this.reportSetting = new ReportSetting(tenantSettingService);
    this.courseLevelGridSetting = new CourseLevelSetting(courseLevelService);

    initUI();
  }

  @Nonnull
  @Override
  protected List<VABaseSetting<?>> createSettings() {
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
