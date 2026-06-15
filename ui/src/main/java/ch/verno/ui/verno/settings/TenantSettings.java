package ch.verno.ui.verno.settings;

import ch.verno.common.gate.GlobalInterface;
import ch.verno.lib.Lazy;
import ch.verno.publ.Routes;
import ch.verno.ui.lib.settings.VABaseSetting;
import ch.verno.ui.lib.settings.VABaseSettingsPage;
import ch.verno.ui.verno.settings.panels.courselevel.CourseLevelSetting;
import ch.verno.ui.verno.settings.panels.gender.GenderSetting;
import ch.verno.ui.verno.settings.panels.mail.MailSettings;
import ch.verno.ui.verno.settings.panels.quantity.QuantitySetting;
import ch.verno.ui.verno.settings.panels.report.ReportSetting;
import ch.verno.ui.verno.settings.panels.shared.SharedSettings;
import ch.verno.ui.verno.settings.panels.subscription.SubscriptionSettings;
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

  @Nonnull private final Lazy<SharedSettings> sharedSetting;
  @Nonnull private final Lazy<QuantitySetting> quantitySetting;
  @Nonnull private final Lazy<ReportSetting> reportSetting;
  @Nonnull private final Lazy<CourseLevelSetting> courseLevelGridSetting;
  @Nonnull private final Lazy<GenderSetting> genderSetting;
  @Nonnull private final Lazy<MailSettings> mailSettings;
  @Nonnull private final Lazy<SubscriptionSettings> subscriptionSettings;

  @Autowired
  public TenantSettings(@Nonnull final GlobalInterface globalInterface) {
    this.sharedSetting = Lazy.of(() -> new SharedSettings(globalInterface));
    this.quantitySetting = Lazy.of(() -> new QuantitySetting(globalInterface));
    this.reportSetting = Lazy.of(() -> new ReportSetting(globalInterface));
    this.courseLevelGridSetting = Lazy.of(() -> new CourseLevelSetting(globalInterface));
    this.genderSetting = Lazy.of(() -> new GenderSetting(globalInterface));
    this.mailSettings = Lazy.of(() -> new MailSettings(globalInterface));
    this.subscriptionSettings = Lazy.of(() -> new SubscriptionSettings(globalInterface));

    initUI(globalInterface);
  }

  @Nonnull
  @Override
  protected List<VABaseSetting<?>> getSettings() {
    return List.of(
            quantitySetting.get(),
            sharedSetting.get(),
            reportSetting.get(),
            courseLevelGridSetting.get(),
            genderSetting.get(),
            mailSettings.get(),
            subscriptionSettings.get()
    );
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
