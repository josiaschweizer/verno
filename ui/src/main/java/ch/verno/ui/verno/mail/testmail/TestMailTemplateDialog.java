package ch.verno.ui.verno.mail.testmail;

import ch.verno.ui.base.components.button.VAButton;
import ch.verno.ui.base.components.dialog.DialogSize;
import ch.verno.ui.base.components.dialog.VAAbstractDialog;
import ch.verno.ui.base.components.entry.email.VAMailField;
import ch.verno.ui.base.components.layout.horizontal.VAHorizontalLayout;
import ch.verno.ui.lib.icon.CustomIcons;
import ch.verno.ui.lib.icon.IconUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import jakarta.annotation.Nonnull;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class TestMailTemplateDialog extends VAAbstractDialog {

  @Nonnull private final Consumer<String> sendEmail;

  @Nonnull private final VAMailField emailField;

  public TestMailTemplateDialog(@Nonnull final Consumer<String> sendEmail) {
    this.sendEmail = sendEmail;
    this.emailField = createEmailField(); // needs to be initialized up here so that we can use it in the createSendButton()

    initUI(getTranslation("mail.test.mail.template"), DialogSize.SMALL);
  }

  @Nonnull
  @Override
  protected VAHorizontalLayout createContent() {

    final var layout = new VAHorizontalLayout(emailField);
    layout.setSizeFull();
    return layout;
  }

  @Nonnull
  private VAMailField createEmailField() {
    final var emailField = new VAMailField(
            getTranslation("mail.recipient.email"),
            getTranslation("mail.enter.your.recipient.email.to.teste.your.e.mail.config")
    );
    emailField.setRequired(true);
    emailField.setSizeFull();
    return emailField;
  }

  @Nonnull
  @Override
  protected Collection<Button> createActionButtons() {
    return List.of(createCancelButton(), createSendButton());
  }

  @Nonnull
  private VAButton createSendButton() {
    final var button = new VAButton(getTranslation("shared.send"), IconUtil.createSmall(CustomIcons.SEND_MAIL));
    button.addClickListener(e -> sendEmail.accept(emailField.getValue()));
    button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    button.setEnabled(isValid());
    emailField.addValueChangeListener(e -> button.setEnabled(isValid()));
    emailField.setValueChangeMode(ValueChangeMode.EAGER);
    return button;
  }


  private boolean isValid() {
    return !emailField.getValue().isBlank() && !emailField.isInvalid();
  }
}
