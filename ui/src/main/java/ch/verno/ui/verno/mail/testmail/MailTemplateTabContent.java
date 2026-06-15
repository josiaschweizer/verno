package ch.verno.ui.verno.mail.testmail;

import ch.verno.common.db.dto.table.mail.MailTemplateDto;
import ch.verno.ui.base.components.layout.vertical.VAVerticalLayout;
import ch.verno.ui.base.components.notification.inline.VAInlineNotification;
import com.vaadin.flow.data.binder.Binder;
import jakarta.annotation.Nonnull;

import javax.annotation.Nullable;
import java.util.Optional;

public class MailTemplateTabContent {

  @Nonnull private final Binder<MailTemplateDto> binder;

  @Nullable private VAVerticalLayout layout;

  @Nonnull private final TestMailTemplatePage.MailTemplateTypeMapping textMapping;

  public MailTemplateTabContent(@Nonnull final TestMailTemplatePage.MailTemplateTypeMapping textMapping) {
    binder = new Binder<>();

    this.textMapping = textMapping;

    initUI();
  }

  private void initUI() {
    final var notification = createInlineNotification();
    final var mailTemplateLayout = createMailTemplateLayout();

    this.layout = new VAVerticalLayout(notification, mailTemplateLayout);
    layout.setSpacing(true);
  }

  @Nonnull
  private VAInlineNotification createInlineNotification() {
    final var notification = new VAInlineNotification();
    notification.setTitle(textMapping.getName());
    notification.setDescription(textMapping.getDescription());
    return notification;
  }

  @Nonnull
  private VAVerticalLayout createMailTemplateLayout() {

    return new VAVerticalLayout();
  }

  @Nonnull
  public VAVerticalLayout getLayout() {
    return Optional.ofNullable(layout).orElse(new VAVerticalLayout());
  }

}
