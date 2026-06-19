package ch.verno.ui.verno.mail.testmail;

import ch.verno.rpc.client.mail.MailClient;
import ch.verno.common.lib.Routes;
import ch.verno.lib.Lazy;
import ch.verno.ui.base.components.layout.vertical.VAVerticalLayout;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.base.navigation.Navigator;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.tabs.TabSheet;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

@PermitAll
@com.vaadin.flow.router.Route(Routes.MAIL_TEST)
public class TestMailTemplatePage extends VAVerticalLayout {

  @Nonnull private final Injector injector;
  @Nonnull private final Lazy<MailClient> mailClient;

  @Inject
  public TestMailTemplatePage(@Nonnull final Injector injector) {
    this.injector = injector;
    this.mailClient = Lazy.of(() -> injector.getInstance(MailClient.class));

    if (!mailConfigValid()) {
      navigateToSetting();
    }

    initUI();
  }

  private void initUI() {
    final var tabs = new TabSheet();
    tabs.setSizeFull();

    for (final var value : MailTemplateTypeMapping.values()) {
      final var tab = new MailTemplateTabContent(injector, value);
      tabs.add(value.getName(injector), tab.getLayout());
    }

    add(tabs);
    setSizeFull();
  }

  private boolean mailConfigValid() {
    final var config = mailClient.get().getOptionalConfigForCurrentTenant();
    return config.isPresent();
  }

  private void navigateToSetting() {
    NotificationFactory.showWarningNotification(getTranslation("mail.config.not.available.you.cannot.enter.the.mail.test.page"));
    UI.getCurrent().beforeClientResponse(this, context -> Navigator.navigateTo(Routes.TENANT_SETTINGS));
  }
}
