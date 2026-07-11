package ch.verno.ui.lib.components.email;

import ch.verno.contract.dto.table.mail.MailTemplateDto;
import ch.verno.contract.mail.MailTemplateType;
import ch.verno.contract.mail.placeholder.course.CoursePlaceholder;
import ch.verno.lib.Lazy;
import ch.verno.lib.VernoUtility;
import ch.verno.rpc.client.mail.MailTemplateClient;
import ch.verno.ui.base.components.layout.horizontal.VAHorizontalLayout;
import ch.verno.ui.base.factory.EntryFactory;
import com.google.inject.Injector;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.i18n.I18NProvider;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Optional;

public abstract class AbstractMailTemplateConfigLayout extends Composite<VAHorizontalLayout> {

  @Nonnull private final Lazy<MailTemplateClient> mailTemplateClient;

  @Nullable private MailTemplateDto template;
  @Nonnull protected final Binder<MailTemplateDto> binder;
  @Nonnull private final MailTemplateType mailTemplateType;

  @Nullable private TextArea selectedTextArea;
  @Nonnull private final EntryFactory<MailTemplateDto> entryFactory;

  public AbstractMailTemplateConfigLayout(@Nonnull final Injector injector,
                                          @Nonnull final MailTemplateType mailTemplateType) {
    this.mailTemplateClient = Lazy.of(() -> injector.getInstance(MailTemplateClient.class));

    this.entryFactory = new EntryFactory<>(injector.getInstance(I18NProvider.class));
    this.mailTemplateType = mailTemplateType;
    this.binder = new Binder<>(MailTemplateDto.class);

    initLayout();
    initBinder();
  }

  private void initLayout() {
    final var emailSubject = entryFactory.createTextAreaEntry(
            MailTemplateDto::getSubject,
            MailTemplateDto::setSubject,
            binder,
            Optional.of(getTranslation("setting.email.subject.is.required")),
            getTranslation("setting.email.subject")
    );
    emailSubject.setWidthFull();
    emailSubject.addFocusListener(e -> selectedTextArea = emailSubject);
    emailSubject.setPlaceholder(getTranslation("setting.no.email.subject.yet.use.the.buttons.on.the.right.to.add.placeholders.and.type.your.own.subject"));

    final var emailContent = entryFactory.createTextAreaEntry(
            MailTemplateDto::getContent,
            MailTemplateDto::setContent,
            binder,
            Optional.of(getTranslation("setting.email.content.is.required")),
            getTranslation("setting.email.content")
    );
    emailContent.addFocusListener(e -> selectedTextArea = emailContent);
    emailContent.setPlaceholder(getTranslation("setting.no.email.content.yet.use.the.buttons.on.the.right.to.add.placeholders.and.type.your.own.content"));
    emailContent.setSizeFull();

    final var emailLayout = new VerticalLayout(emailSubject, emailContent);
    emailLayout.setPadding(false);
    emailLayout.setSpacing(false);
    emailLayout.getStyle().setMinWidth(VernoUtility.NONE);
    emailLayout.setSizeFull();

    final var placeholderLayout = createPlaceholderLayout();
    placeholderLayout.setWidth(null);
    placeholderLayout.setFlexShrink(0);

    final var contentLayout = getContent();
    contentLayout.add(emailLayout, placeholderLayout);
    contentLayout.setSizeFull();
    contentLayout.setPadding(false);
    contentLayout.setSpacing(true);
    contentLayout.setFlexGrow(1, emailLayout);
    contentLayout.setFlexGrow(0, placeholderLayout);
  }

  private void initBinder() {
    binder.setChangeDetectionEnabled(true);

    final var templateByKey = mailTemplateClient.get().getTemplateByKey(mailTemplateType.getKey());
    template = templateByKey.orElseGet(() -> MailTemplateDto.fromTemplateKey(mailTemplateType.getKey()));

    binder.readBean(template);
  }

  public void addBinderValueChangeListener(@Nonnull final HasValue.ValueChangeListener<HasValue.ValueChangeEvent<?>> event) {
    binder.addValueChangeListener(event);
  }

  public boolean isValid() {
    return binder.isValid();
  }

  public boolean hasChanges() {
    return binder.hasChanges();
  }

  @Nonnull
  public MailTemplateDto getBean() {
    final var bean = MailTemplateDto.fromTemplateKey(mailTemplateType.getKey());

    try {
      binder.writeBean(bean);
    } catch (ValidationException e) {
      throw new IllegalStateException("Mail template is invalid.", e);
    }

    return bean;
  }

  public void saveTemplate() {
    if (!binder.isValid() || template == null) {
      return;
    }

    try {
      binder.writeBean(template);
    } catch (ValidationException e) {
      return;
    }

    mailTemplateClient.get().saveMailTemplate(template);
    binder.readBean(template); // read template dto so we no longer have changes
  }

  public void addStatusChangeListener(@Nonnull final Runnable listener) {
    binder.addStatusChangeListener(e -> listener.run());
  }

  @Nonnull
  protected Button createPlaceholderButton(@Nonnull final CoursePlaceholder placeholder) {
    final var btn = new Button(getTranslation(placeholder.getNameKey()));
    btn.addClickListener(e -> {
      if (selectedTextArea != null) {
        selectedTextArea.setValue(selectedTextArea.getValue() + placeholder.getValue());
        selectedTextArea.focus();
      }
    });
    return btn;
  }

  @Nonnull
  private VerticalLayout createPlaceholderLayout() {
    return new VerticalLayout(createPlaceholderButtons().toArray(new Component[0]));
  }

  @Override
  protected void onAttach(@Nonnull final AttachEvent attachEvent) {
    final var bean = binder.getBean();
    if (bean != null) {
      binder.readBean(bean);
    }
  }

  @Nonnull
  protected abstract List<Button> createPlaceholderButtons();
}