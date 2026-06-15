package ch.verno.ui.verno.mail.testmail;

import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.lib.i18n.TranslationHelper;
import ch.verno.ui.base.components.button.VAButton;
import ch.verno.ui.base.components.layout.horizontal.VAHorizontalLayout;
import ch.verno.ui.base.components.layout.vertical.VAVerticalLayout;
import ch.verno.ui.base.components.notification.inline.VAInlineNotification;
import ch.verno.ui.lib.icon.CustomIcons;
import ch.verno.ui.lib.icon.IconUtil;
import ch.verno.ui.verno.dashboard.mail.CourseMailTemplateConfigLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import jakarta.annotation.Nonnull;

import javax.annotation.Nullable;

public class MailTemplateTabContent {

  @Nonnull private final GlobalInterface globalInterface;
  @Nonnull private final MailTemplateTypeMapping textMapping;

  @Nonnull private final VAVerticalLayout layout;
  @Nullable private CourseMailTemplateConfigLayout templateLayout;

  public MailTemplateTabContent(@Nonnull final GlobalInterface globalInterface,
                                @Nonnull final MailTemplateTypeMapping mapping) {
    this.globalInterface = globalInterface;
    this.textMapping = mapping;

    this.layout = initUI();
  }

  @Nonnull
  private VAVerticalLayout initUI() {
    final var notification = createInlineNotification();
    this.templateLayout = createMailTemplateLayout();
    final var actionLayout = createActionLayout();

    final var layout = new VAVerticalLayout(notification, templateLayout, actionLayout);
    layout.setSpacing(true);
    layout.setSizeFull();
    return layout;
  }

  @Nonnull
  private VAInlineNotification createInlineNotification() {
    final var notification = new VAInlineNotification();
    notification.setTitle(textMapping.getName(globalInterface));
    notification.setDescription(textMapping.getDescription(globalInterface));
    return notification;
  }

  @Nonnull
  private CourseMailTemplateConfigLayout createMailTemplateLayout() {
    final var config = new CourseMailTemplateConfigLayout(globalInterface, textMapping.getMailTemplateType());
    config.getContent().setSizeFull();
    return config;
  }

  @Nonnull
  private VAHorizontalLayout createActionLayout() {
    final var testMailButton = createTestMailButton();
    final var saveButton = createSaveButton();

    final var layout = new VAHorizontalLayout(testMailButton, saveButton);
    layout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
    layout.setAlignItems(FlexComponent.Alignment.CENTER);
    layout.setSpacing(true);
    layout.setWidthFull();

    return layout;
  }

  @Nonnull
  public VAVerticalLayout getLayout() {
    return layout;
  }

  @Nonnull
  private VAButton createTestMailButton() {
    final var button = new VAButton(TranslationHelper.getTranslation(globalInterface, "mail.test.mail.template"), IconUtil.createExtraSmall(VaadinIcon.PLAY));
    button.addClickListener(e -> testMail());
    return button;
  }

  @Nonnull
  private VAButton createSaveButton() {
    final var button = new VAButton(TranslationHelper.getTranslation(globalInterface, "common.save"), IconUtil.creatExtraSmall(CustomIcons.SAVE));
    button.addClickListener(e -> saveTemplate());
    return button;
  }

  private void testMail() {
    final var dialog = new TestMailTemplateDialog(this::sendEmail);
    dialog.open();
  }

  private void sendEmail(@Nonnull final String recipient) {
    //TODO tdb
  }

  public void saveTemplate() {
    templateLayout.saveTemplate();
  }

}
