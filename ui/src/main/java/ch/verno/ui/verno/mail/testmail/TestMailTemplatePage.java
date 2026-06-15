package ch.verno.ui.verno.mail.testmail;

import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.lib.mail.MailTemplateType;
import ch.verno.common.server.service.intern.mail.IMailConfigService;
import ch.verno.common.server.service.intern.mail.IMailTemplateService;
import ch.verno.lib.Lazy;
import ch.verno.publ.Routes;
import ch.verno.ui.base.components.layout.vertical.VAVerticalLayout;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.base.components.notification.inline.VAInlineNotification;
import ch.verno.ui.base.navigation.Navigator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

import javax.annotation.Nullable;

@PermitAll
@Route(Routes.MAIL_TEST)
public class TestMailTemplatePage extends VerticalLayout {

  @Nonnull private final Lazy<IMailConfigService> mailConfigService;
  @Nonnull private final Lazy<IMailTemplateService> mailTemplateService;

  public TestMailTemplatePage(@Nonnull final GlobalInterface globalInterface) {
    this.mailConfigService = Lazy.of(() -> globalInterface.getService(IMailConfigService.class));
    this.mailTemplateService = Lazy.of(() -> globalInterface.getService(IMailTemplateService.class));

    if (!mailConfigValid()) {
      navigateToSetting();
    }

    initUI();
  }

  private void initUI() {
    final var tabs = new TabSheet();

    for (final var value : MailTemplateType.values()) {
      tabs.add(value.name(), createTabContent(value));
    }
  }

  @Nonnull
  private VAVerticalLayout createTabContent(@Nonnull final MailTemplateType mailTemplateType) {
    final var textMapping = MailTemplateTypeMapping.fromType(mailTemplateType);

    final var inlineNotification = new VAInlineNotification();
    inlineNotification.setTitle(textMapping.name());
    inlineNotification.setDescription(textMapping.getDescription());



    final var layout = new VAVerticalLayout();
    layout.add(inlineNotification);
    return layout;
  }

  private boolean mailConfigValid() {
    final var config = mailConfigService.get().getOptionalConfigForCurrentTenant();
    return config.isPresent();
  }

  private void navigateToSetting() {
    NotificationFactory.showWarningNotification("Mail Config not available - you cannot enter the mail test page.");
    UI.getCurrent().beforeClientResponse(this, context -> {
      Navigator.navigateTo(Routes.TENANT_SETTINGS);
    });
  }

   enum MailTemplateTypeMapping {
    WELCOME(MailTemplateType.WELCOME, "Welcome Mail", "The welcomee Mail is sent when xy."),
    COURSE_INVITE(MailTemplateType.COURSE_INVITE, "Course Invitation", "The Course Invitation is sent when xy."),
    COURSE_REMINDER(MailTemplateType.COURSE_REMINDER, "Course Reminder", "The Course Reminder is sent when xy.");

    @Nonnull private final MailTemplateType mailTemplateType;
    @Nonnull private final String name;
    @Nonnull private final String description;

    MailTemplateTypeMapping(@Nonnull final MailTemplateType mailTemplateType,
                            @Nonnull final String name,
                            @Nonnull final String description) {
      this.mailTemplateType = mailTemplateType;
      this.name = name;
      this.description = description;
    }

    @Nonnull
    public MailTemplateType getMailTemplateType() {
      return mailTemplateType;
    }

    @Nonnull
    public String getName() {
      return name;
    }

    @Nonnull
    public String getDescription() {
      return description;
    }

    @Nullable
    public static MailTemplateTypeMapping fromType(@Nonnull final MailTemplateType mailTemplateType) {
      for (final var value : values()) {
        if (mailTemplateType.equals(value.getMailTemplateType())) {
          return value;
        }
      }

      throw new IllegalArgumentException("Unknown mail template type: " + mailTemplateType);
    }

  }
}
