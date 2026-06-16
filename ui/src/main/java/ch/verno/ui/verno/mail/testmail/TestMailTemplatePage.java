package ch.verno.ui.verno.mail.testmail;

import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.server.service.intern.mail.IMailConfigService;
import ch.verno.lib.Lazy;
import ch.verno.publ.Routes;
import ch.verno.ui.base.components.layout.vertical.VAVerticalLayout;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.base.navigation.Navigator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(Routes.MAIL_TEST)
public class TestMailTemplatePage extends VAVerticalLayout {

  @Nonnull private final GlobalInterface globalInterface;
  @Nonnull private final Lazy<IMailConfigService> mailConfigService;

  public TestMailTemplatePage(@Nonnull final GlobalInterface globalInterface) {
    this.globalInterface = globalInterface;
    this.mailConfigService = Lazy.of(() -> globalInterface.getService(IMailConfigService.class));

    if (!mailConfigValid()) {
      navigateToSetting();
    }

    initUI();
  }

  private void initUI() {
    final var tabs = new TabSheet();
    tabs.setSizeFull();

    for (final var value : MailTemplateTypeMapping.values()) {
      final var tab = new MailTemplateTabContent(globalInterface, value);
      tabs.add(value.getName(globalInterface), tab.getLayout());
    }

    add(tabs);
    setSizeFull();
  }

  private boolean mailConfigValid() {
    final var config = mailConfigService.get().getOptionalConfigForCurrentTenant();
    return config.isPresent();
  }

  private void navigateToSetting() {
    NotificationFactory.showWarningNotification(getTranslation("mail.config.not.available.you.cannot.enter.the.mail.test.page"));
    UI.getCurrent().beforeClientResponse(this, context -> Navigator.navigateTo(Routes.TENANT_SETTINGS));
  }
}
