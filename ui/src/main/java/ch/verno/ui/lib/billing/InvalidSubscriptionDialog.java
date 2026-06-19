package ch.verno.ui.lib.billing;

import ch.verno.common.db.dto.table.billing.TenantBillingDto;
import ch.verno.common.server.service.extern.ITenantBillingService;
import ch.verno.common.server.service.extern.billing.token.IBillingAccessLinkService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.properties.UserProperties;
import ch.verno.common.lib.application.RunMode;
import ch.verno.common.tenant.TenantContext;
import ch.verno.lib.Publ;
import ch.verno.server.applicationproperties.application.VernoApplicationConfigProviderImpl;
import ch.verno.ui.base.components.anchorbutton.VAAnchorButton;
import ch.verno.ui.base.components.button.VAButton;
import ch.verno.ui.base.components.dialog.DialogSize;
import ch.verno.ui.base.components.dialog.VAAbstractDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ModalityMode;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@CssImport("./apps/invalid-subscription/invalid-subscription-dialog.css")
public class InvalidSubscriptionDialog extends VAAbstractDialog {

  public static final String CLASSNAME_INVALID_SUBSCRIPTION_DIALOG = "invalid-subscription-dialog";
  public static final String CLASSNAME_INVALID_SUBSCRIPTION_DIALOG_ICON = "invalid-subscription-dialog-icon";
  public static final String CLASSNAME_INVALID_SUBSCRIPTION_DIALOG_HINT = "invalid-subscription-dialog-hint";
  public static final String CLASSNAME_INVALID_SUBSCRIPTION_DIALOG_BUTTON = "invalid-subscription-dialog-button";
  public static final String CLASSNAME_INVALID_SUBSCRIPTION_DIALOG_TEXT = "invalid-subscription-dialog-text";
  public static final String CLASSNAME_INVALID_SUBSCRIPTION_DIALOG_CONTENT = "invalid-subscription-dialog-content";

  @Nonnull private final UserProperties userProperties;
  @Nonnull private final ITenantBillingService tenantBillingService;
  @Nonnull private final VernoApplicationConfigProviderImpl applicationConfig;
  @Nonnull private final IBillingAccessLinkService billingAccessLinkService;


  public InvalidSubscriptionDialog(@Nonnull final GlobalInterface globalInterface) {
    userProperties = globalInterface.getUserProperties();
    tenantBillingService = globalInterface.getService(ITenantBillingService.class);
    applicationConfig = globalInterface.getService(VernoApplicationConfigProviderImpl.class);
    billingAccessLinkService = globalInterface.getService(IBillingAccessLinkService.class);

    setCloseOnEsc(false);
    setCloseOnOutsideClick(false);
    setModality(ModalityMode.STRICT);
    addClassName(CLASSNAME_INVALID_SUBSCRIPTION_DIALOG);

    initUI(null, DialogSize.SMALL);
  }

  @Nonnull
  @Override
  protected HorizontalLayout createContent() {
    final var wrapper = new HorizontalLayout(buildContent());
    wrapper.setWidthFull();
    wrapper.setPadding(false);
    wrapper.setSpacing(false);
    wrapper.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    return wrapper;
  }

  @Nonnull
  private Component buildContent() {
    final var warningIcon = VaadinIcon.WARNING.create();
    warningIcon.addClassName(CLASSNAME_INVALID_SUBSCRIPTION_DIALOG_ICON);

    final var headline = new H3(getTranslation("base.your.subscription.is.no.longer.valid"));
    headline.getStyle().setMargin("0");

    final var text = new Paragraph(getTranslation("base.to.continue.using.verno.please.review.your.billing.details.and.update.your.subscription"));
    text.getStyle().setMargin("0");

    final var hint = new Paragraph(getTranslation("base.once.your.subscription.is.active.again.you.can.continue.as.usual"));
    hint.getStyle().setMargin("0");
    hint.addClassName(CLASSNAME_INVALID_SUBSCRIPTION_DIALOG_HINT);

    VAButton manageSubscriptionButton;
    if (RunMode.fromKey(applicationConfig.getRunMode()).equals(RunMode.DEV)) {
      manageSubscriptionButton = new VAButton(
              VaadinIcon.CREDIT_CARD.create(),
              "DEV Mode - Create Dev Subscription - only for DEV Usage!!!",
              e -> createDevSubscription()
      );
    } else {
      manageSubscriptionButton = new VAAnchorButton(
              VaadinIcon.EXTERNAL_LINK.create(),
              getTranslation("base.manage.subscription"),
              getRedirectLink()
      );
    }

    manageSubscriptionButton.addClassName(CLASSNAME_INVALID_SUBSCRIPTION_DIALOG_BUTTON);

    final var textBlock = new Div(headline, text, hint);
    textBlock.addClassName(CLASSNAME_INVALID_SUBSCRIPTION_DIALOG_TEXT);

    final var content = new VerticalLayout(warningIcon, textBlock, manageSubscriptionButton);
    content.setPadding(false);
    content.setSpacing(false);
    content.setWidthFull();
    content.setAlignItems(FlexComponent.Alignment.CENTER);
    content.addClassName(CLASSNAME_INVALID_SUBSCRIPTION_DIALOG_CONTENT);

    return content;
  }

  @Nonnull
  private String getRedirectLink() {
    final var currentUser = userProperties.getCurrentUser();

    return billingAccessLinkService.createSubscriptionUrlForCheckout(
            TenantContext.getRequired(),
            Optional.ofNullable(currentUser.getId()).orElse(Publ.ZERO_LONG)
    );
  }

  private void createDevSubscription() {
    final var currentTenantId = TenantContext.getOrDefault(Publ.ZERO_LONG);
    tenantBillingService.createTenantBilling(TenantBillingDto.createDefaultDevDto(currentTenantId));

    Optional.of(UI.getCurrent()).ifPresent(ui -> ui.refreshCurrentRoute(false));
  }

  @Nonnull
  @Override
  protected Collection<Button> createActionButtons() {
    return List.of();
  }
}