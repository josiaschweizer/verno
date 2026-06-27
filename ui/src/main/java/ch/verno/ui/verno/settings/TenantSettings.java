package ch.verno.ui.verno.settings;

import ch.verno.common.lib.Routes;
import ch.verno.lib.Lazy;
import ch.verno.ui.lib.settings.VABaseSetting;
import ch.verno.ui.lib.settings.VABaseSettingsPage;
import ch.verno.ui.verno.settings.panels.courselevel.CourseLevelSetting;
import ch.verno.ui.verno.settings.panels.gender.GenderSetting;
import ch.verno.ui.verno.settings.panels.mail.MailSettings;
import ch.verno.ui.verno.settings.panels.quantity.QuantitySetting;
import ch.verno.ui.verno.settings.panels.report.ReportSetting;
import ch.verno.ui.verno.settings.panels.shared.SharedSettings;
import ch.verno.ui.verno.settings.panels.subscription.SubscriptionSettings;
import com.google.inject.Injector;
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
  public TenantSettings(@Nonnull final Injector injector) {
    this.sharedSetting = Lazy.of(() -> injector.getInstance(SharedSettings.class));
    this.quantitySetting = Lazy.of(() -> injector.getInstance(QuantitySetting.class));
    this.reportSetting = Lazy.of(() -> injector.getInstance(ReportSetting.class));
    this.courseLevelGridSetting = Lazy.of(() -> injector.getInstance(CourseLevelSetting.class));
    this.genderSetting = Lazy.of(() -> injector.getInstance(GenderSetting.class));
    this.mailSettings = Lazy.of(() -> injector.getInstance(MailSettings.class));
    this.subscriptionSettings = Lazy.of(() -> injector.getInstance(SubscriptionSettings.class));

    initUI(injector);
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
