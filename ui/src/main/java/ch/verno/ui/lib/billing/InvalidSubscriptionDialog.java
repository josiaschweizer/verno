package ch.verno.ui.lib.billing;

import ch.verno.common.lib.application.RunMode;
import ch.verno.common.tenant.TenantContext;
import ch.verno.contract.dto.table.billing.TenantBillingDto;
import ch.verno.lib.CssImportConstants;
import ch.verno.lib.Lazy;
import ch.verno.lib.Publ;
import ch.verno.rpc.client.billing.BillingClient;
import ch.verno.rpc.properties.application.ApplicationProperties;
import ch.verno.rpc.properties.user.UserProperties;
import ch.verno.ui.base.components.anchorbutton.VAAnchorButton;
import ch.verno.ui.base.components.button.VAButton;
import ch.verno.ui.base.components.dialog.DialogSize;
import ch.verno.ui.base.components.dialog.VAAbstractDialog;
import com.google.inject.Inject;
import com.google.inject.Injector;
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
import org.jetbrains.annotations.NonNls;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@CssImport(CssImportConstants.INVALID_SUBSCRIPTION_DIALOG)
public class InvalidSubscriptionDialog extends VAAbstractDialog {

  @NonNls public static final String CLASSNAME_INVALID_SUBSCRIPTION_DIALOG = "invalid-subscription-dialog";
  @NonNls public static final String CLASSNAME_INVALID_SUBSCRIPTION_DIALOG_ICON = "invalid-subscription-dialog-icon";
  @NonNls public static final String CLASSNAME_INVALID_SUBSCRIPTION_DIALOG_HINT = "invalid-subscription-dialog-hint";
  @NonNls public static final String CLASSNAME_INVALID_SUBSCRIPTION_DIALOG_BUTTON = "invalid-subscription-dialog-button";
  @NonNls public static final String CLASSNAME_INVALID_SUBSCRIPTION_DIALOG_TEXT = "invalid-subscription-dialog-text";
  @NonNls public static final String CLASSNAME_INVALID_SUBSCRIPTION_DIALOG_CONTENT = "invalid-subscription-dialog-content";

  @Nonnull private final Lazy<BillingClient> billingClient;
  @Nonnull private final Lazy<UserProperties> userProperties;
  @Nonnull private final Lazy<ApplicationProperties> applicationProperties;

  @Inject
  public InvalidSubscriptionDialog(@Nonnull final Injector injector) {
    this.billingClient = Lazy.of(() -> injector.getInstance(BillingClient.class));
    this.userProperties = Lazy.of(() -> injector.getInstance(UserProperties.class));
    this.applicationProperties = Lazy.of(() -> injector.getInstance(ApplicationProperties.class));

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
    if (applicationProperties.get().getRunMode().equals(RunMode.DEV)) {
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
    final var currentUser = userProperties.get().getCurrentAppUser();

    return billingClient.get().createSubscriptionUrlForCheckout(Optional.ofNullable(currentUser.getId()).orElse(Publ.ZERO_LONG));
  }

  private void createDevSubscription() {
    final var currentTenantId = TenantContext.getOrDefault(Publ.ZERO_LONG);
    billingClient.get().createTenantBilling(TenantBillingDto.createDefaultDevDto(currentTenantId));

    Optional.of(UI.getCurrent()).ifPresent(ui -> ui.refreshCurrentRoute(false));
  }

  @Nonnull
  @Override
  protected Collection<Button> createActionButtons() {
    return List.of();
  }
}