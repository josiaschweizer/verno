package ch.verno.ui.verno.settings.panels.mail;

import ch.verno.common.tenant.TenantContext;
import ch.verno.common.type.mail.MailValidity;
import ch.verno.lib.Lazy;
import ch.verno.lib.Publ;
import ch.verno.lib.VernoUtility;
import ch.verno.rpc.client.mail.MailClient;
import ch.verno.rpc.client.mail.MailConfigClient;
import ch.verno.ui.base.components.dialog.DialogSize;
import ch.verno.ui.base.components.dialog.VAAbstractDialog;
import ch.verno.ui.verno.settings.panels.mail.mailtest.TestResult;
import ch.verno.ui.verno.settings.panels.mail.mailtest.TestStatus;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class TestConnectionDialog extends VAAbstractDialog {

  @Nonnull private final Lazy<MailClient> mailClient;
  @Nonnull private final Lazy<MailConfigClient> mailConfigClient;

  @Nullable private EmailField emailField;
  @Nullable private ProgressBar progressBar;

  @Nullable private Div resultBox;
  @Nullable private HorizontalLayout resultHeader;
  @Nullable private Icon resultIcon;
  @Nullable private Span resultTitle;
  @Nullable private Span resultMessage;

  @Nullable private Button closeButton;
  @Nullable private Button testButton;

  private volatile boolean running = false;

  @Inject
  public TestConnectionDialog(@Nonnull final Injector injector) {
    this.mailClient = Lazy.of(() -> injector.getInstance(MailClient.class));
    this.mailConfigClient = Lazy.of(() -> injector.getInstance(MailConfigClient.class));

    initUI(getTranslation("setting.test.email.connection"), DialogSize.MEDIUM);
  }

  @Override
  public void open() {
    resetState();
    super.open();
  }

  @Nonnull
  @Override
  protected HorizontalLayout createContent() {
    emailField = new EmailField(getTranslation("setting.test.email"));
    emailField.setWidthFull();
    emailField.setClearButtonVisible(true);
    emailField.setRequiredIndicatorVisible(true);

    progressBar = new ProgressBar();
    progressBar.setWidthFull();
    progressBar.setIndeterminate(true);
    progressBar.setVisible(false);

    resultIcon = VaadinIcon.INFO_CIRCLE_O.create();

    resultTitle = new Span();
    resultTitle.getStyle().setFontWeight("600");

    resultMessage = new Span();
    resultMessage.getStyle().setWhiteSpace(Style.WhiteSpace.PRE_WRAP);
    resultMessage.getStyle().set("overflow-wrap", "anywhere");

    resultHeader = new HorizontalLayout(resultIcon, resultTitle);
    resultHeader.setAlignItems(HorizontalLayout.Alignment.CENTER);
    resultHeader.setPadding(false);
    resultHeader.setSpacing(true);
    resultHeader.setWidthFull();

    final var resultBody = new VerticalLayout(resultHeader, resultMessage);
    resultBody.setPadding(false);
    resultBody.setSpacing(false);
    resultBody.getStyle().setGap(VernoUtility.LUMO_SPACE_XS);
    resultBody.setWidthFull();

    resultBox = new Div(resultBody);
    resultBox.setWidthFull();
    resultBox.setVisible(false);
    resultBox.getStyle().setBorderRadius("var(--lumo-border-radius-l)");
    resultBox.getStyle().setPadding("var(--lumo-space-m)");
    resultBox.getStyle().setBorder("1px solid var(--lumo-contrast-20pct)");
    resultBox.getStyle().setBackground("var(--lumo-contrast-5pct)");
    resultBox.getStyle().set("box-sizing", "border-box");
    resultBox.getStyle().setMaxWidth("100%");
    resultBox.getStyle().setOverflow(Style.Overflow.HIDDEN);

    final var root = new VerticalLayout(emailField, progressBar, resultBox);
    root.setPadding(false);
    root.setSpacing(true);
    root.setWidthFull();
    root.getStyle().setGap("var(--lumo-space-m)");

    final var wrapper = new HorizontalLayout(root);
    wrapper.setWidthFull();
    return wrapper;
  }

  @Nonnull
  @Override
  protected Collection<Button> createActionButtons() {
    closeButton = new Button(getTranslation("shared.cancel"));
    closeButton.addClickListener(e -> {
      if (!running) {
        close();
      }
    });

    testButton = new Button(getTranslation("setting.send.test.email"));
    testButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    testButton.addClickListener(e -> onTestClicked());

    return List.of(closeButton, testButton);
  }

  private void onTestClicked() {
    if (running) {
      return;
    }

    final var executor = Executors.newSingleThreadExecutor(r -> {
      final var thread = new Thread(r, "mail-test-connection");
      thread.setDaemon(true);
      return thread;
    });

    final var email = Objects.toString(emailField.getValue(), Publ.EMPTY_STRING).trim();
    if (email.isBlank() || !email.contains("@")) {
      showResult(new TestResult(TestStatus.INVALID, getTranslation("base.please.enter.a.valid.email.address")));
      return;
    }

    final var nullableUI = UI.getCurrent();
    final var session = VaadinSession.getCurrent();
    final var tenantId = TenantContext.get();

    // resolve translations in ui thread before async call
    final var successMsg = getTranslation("setting.configuration.is.valid.test.email.sent.to.0", email);
    final var errorMsgTemplate = getTranslation("setting.configuration.is.not.valid.0.1", "{0}", "{1}");

    running = true;
    setLoading(true);
    showResult(new TestResult(TestStatus.VALID, Publ.EMPTY_STRING));

    CompletableFuture
            .supplyAsync(() -> {
              VaadinSession.setCurrent(session);
              if (tenantId != null) {
                TenantContext.set(tenantId);
              }
              try {
                return sendTestMail(email, successMsg, errorMsgTemplate);
              } finally {
                TenantContext.clear();
              }
            }, executor)
            .handle((res, ex) -> {
              final TestResult finalRes;
              if (ex != null) {
                final var msg = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
                finalRes = new TestResult(TestStatus.INVALID, getTranslation("setting.test.failed.0", Objects.toString(msg, "unknown error")));
              } else {
                finalRes = res;
              }

              Optional.ofNullable(nullableUI).ifPresent(ui -> ui.access(() -> {
                showResult(finalRes);
                updateButtonText();
                setLoading(false);
                running = false;
                ui.push();
              }));

              if (tenantId != null) {
                TenantContext.set(tenantId);
              }
              try {
                setMailConfigEnabled(finalRes.isValid());
              } finally {
                TenantContext.clear();
              }

              return null;
            });
  }

  @Nonnull
  private TestResult sendTestMail(@Nonnull final String toEmail,
                                  @Nonnull final String successMsg,
                                  @Nonnull final String errorMsgTemplate) {
    try {
      mailClient.get().sendWelcomeMail(toEmail);
      return new TestResult(TestStatus.VALID, successMsg);
    } catch (Exception e) {
      final var message = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
      return new TestResult(TestStatus.INVALID, errorMsgTemplate.replace("{0}", Publ.COLON + Publ.SPACE).replace("{1}", message));
    }
  }

  private void setLoading(final boolean loading) {
    progressBar.setVisible(loading);
    testButton.setEnabled(!loading);
    emailField.setEnabled(!loading);
    closeButton.setEnabled(!loading);
  }

  private void showResult(@Nonnull final TestResult result) {
    if (result.message().isBlank()) {
      resultBox.setVisible(false);
      resultTitle.setText(Publ.EMPTY_STRING);
      resultMessage.setText(Publ.EMPTY_STRING);
      return;
    }

    resultBox.setVisible(true);
    resultMessage.setText(result.message());

    if (result.status() == TestStatus.VALID) {
      setResultIcon(VaadinIcon.CHECK_CIRCLE);
      resultTitle.setText(getTranslation("shared.success"));
      applyResultTheme(true);
    } else {
      setResultIcon(VaadinIcon.CLOSE_CIRCLE);
      resultTitle.setText(getTranslation("shared.failed"));
      applyResultTheme(false);
    }
  }

  private void updateButtonText() {
    closeButton.setText(getTranslation("setting.close"));
  }

  private void setResultIcon(@Nonnull final VaadinIcon icon) {
    resultHeader.replace(resultIcon, resultIcon = icon.create());
  }

  private void applyResultTheme(final boolean success) {
    if (success) {
      resultBox.getStyle().setBackground("var(--lumo-success-color-10pct)");
      resultBox.getStyle().setBorder("1px solid var(--lumo-success-color-50pct)");

      resultTitle.getStyle().setColor("var(--lumo-success-text-color)");
      resultIcon.getStyle().setColor("var(--lumo-success-text-color)");
      resultMessage.getStyle().setColor("var(--lumo-body-text-color)");
    } else {
      resultBox.getStyle().setBackground("var(--lumo-error-color-10pct)");
      resultBox.getStyle().setBorder("1px solid var(--lumo-error-color-50pct)");

      resultTitle.getStyle().setColor("var(--lumo-error-text-color)");
      resultIcon.getStyle().setColor("var(--lumo-error-text-color)");
      resultMessage.getStyle().setColor("var(--lumo-body-text-color)");
    }
  }

  private void resetState() {
    running = false;

    if (emailField != null) {
      emailField.setValue(Publ.EMPTY_STRING);
      emailField.setEnabled(true);
    }
    if (progressBar != null) {
      progressBar.setVisible(false);
    }
    if (testButton != null) {
      testButton.setEnabled(true);
    }
    if (closeButton != null) {
      closeButton.setEnabled(true);
    }

    if (resultBox != null) {
      resultBox.setVisible(false);
    }
    if (resultTitle != null) {
      resultTitle.setText(Publ.EMPTY_STRING);
      resultTitle.getStyle().remove("color");
    }
    if (resultMessage != null) {
      resultMessage.setText(Publ.EMPTY_STRING);
      resultMessage.getStyle().remove("color");
    }
    if (resultIcon != null) {
      resultIcon.getStyle().remove("color");
    }
  }

  private void setMailConfigEnabled(final boolean valid) {
    mailConfigClient.get().updateCurrentMailValidity(valid ? MailValidity.TESTED_VALID : MailValidity.TESTED_INVALID);
  }
}