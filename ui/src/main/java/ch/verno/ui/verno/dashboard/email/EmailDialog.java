package ch.verno.ui.verno.dashboard.email;

import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.lib.mail.placeholder.Placeholder;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.base.dialog.DialogSize;
import ch.verno.ui.base.dialog.VADialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Collection;
import java.util.List;

public class EmailDialog extends VADialog {

  @Nullable private TextArea selectedTextArea;

  public EmailDialog(@Nonnull final GlobalInterface globalInterface) {
    initUI("Send Email", DialogSize.BIG);

    selectedTextArea = null;
  }

  @Nonnull
  @Override
  protected HorizontalLayout createContent() {
    final var emailSubject = new TextArea();
    emailSubject.addFocusListener(event -> selectedTextArea = emailSubject);
    emailSubject.setLabel("Email Subject");
    emailSubject.setPlaceholder("No email subject yet. Use the buttons on the right to add placeholders and type your own subject.");
    emailSubject.setWidthFull();

    final var emailContent = new TextArea();
    emailContent.addFocusListener(event -> selectedTextArea = emailContent);
    emailContent.setLabel("Email Content");
    emailContent.setPlaceholder("No email content yet. Use the buttons on the right to add placeholders and type your own content.");
    emailContent.setWidthFull();
    emailContent.setHeightFull();
    emailContent.setSizeFull();

    final var emailLayout = new VerticalLayout(emailSubject, emailContent);
    emailLayout.setPadding(false);
    emailLayout.setSpacing(false);
    emailLayout.getStyle().set("min-width", "0");
    emailLayout.setWidthFull();
    emailLayout.setHeightFull();

    final var placeholderLayout = createPlaceholderLayout();

    placeholderLayout.setWidth(null);
    placeholderLayout.setFlexShrink(0);

    final var contentLayout = new HorizontalLayout(emailLayout, placeholderLayout);
    contentLayout.setSizeFull();
    contentLayout.setPadding(false);
    contentLayout.setSpacing(true);

    contentLayout.setFlexGrow(1, emailLayout);
    contentLayout.setFlexGrow(0, placeholderLayout);

    return contentLayout;
  }

  @Nonnull
  private VerticalLayout createPlaceholderLayout() {
    final var firstname = createPlaceholderButton(Placeholder.FIRSTNAME);
    final var lastname = createPlaceholderButton(Placeholder.LASTNAME);

    return new VerticalLayout(firstname, lastname);
  }

  @Nonnull
  private Button createPlaceholderButton(@Nonnull final Placeholder placeholder) {
    final var placeholderButton = new Button(getTranslation(placeholder.getNameKey()));
    placeholderButton.addClickListener(e -> {
      if (selectedTextArea != null) {
        selectedTextArea.setValue(selectedTextArea.getValue() + placeholder.getValue());
      }
    });
    return placeholderButton;
  }

  @Nonnull
  @Override
  protected Collection<Button> createActionButtons() {
    final var cancelButton = new Button(getTranslation("shared.cancel"), e -> close());
    final var downloadButton = createSendButton();

    return List.of(cancelButton, downloadButton);
  }

  @Nonnull
  private Button createSendButton() {
    final var sendButton = new Button(getTranslation("shared.send"));
    sendButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    sendButton.addClickListener(e -> {

      NotificationFactory.showInfoNotification("Not implemented yet.");
    });
    return sendButton;
  }
}
