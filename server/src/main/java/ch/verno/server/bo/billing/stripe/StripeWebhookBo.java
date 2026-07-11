package ch.verno.server.bo.billing.stripe;

import ch.verno.common.type.billing.BillingPaymentStatus;
import ch.verno.common.type.billing.BillingPlanKey;
import ch.verno.common.type.billing.BillingSubscriptionStatus;
import ch.verno.common.type.billing.BillingWebhookEventStatus;
import ch.verno.contract.dto.table.billing.BillingWebhookEventDto;
import ch.verno.contract.dto.table.billing.TenantBillingDto;
import ch.verno.lib.Lazy;
import ch.verno.lib.VernoConstants;
import ch.verno.lib.VernoSecrets;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.env.EnvironmentVariableBo;
import ch.verno.server.bo.table.billing.TenantBillingBo;
import ch.verno.server.service.entity.billing.BillingWebhookEventService;
import ch.verno.server.service.entity.billing.TenantBillingService;
import ch.verno.server.type.billing.BillingPlanResolver;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.model.InvoicePayment;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import jakarta.annotation.Nonnull;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;

public class StripeWebhookBo {

  @Nonnull private final Lazy<TenantBillingService> tenantBillingService;
  @Nonnull private final Lazy<EnvironmentVariableBo> environmentVariableBo;
  @Nonnull private final Lazy<BillingWebhookEventService> billingWebhookEventService;

  protected StripeWebhookBo(@Nonnull final ServerBean serverBean) {
    this.tenantBillingService = Lazy.of(() -> serverBean.get(TenantBillingService.class));
    this.environmentVariableBo = Lazy.of(() -> BoFactory.getInstance(serverBean).getEmptyConstructor(EnvironmentVariableBo.class));
    this.billingWebhookEventService = Lazy.of(() -> serverBean.get(BillingWebhookEventService.class));
  }


  public void handleStripeWebhook(@Nonnull final String payload,
                                  @Nonnull final String signatureHead) {
    final var stripeWebhookSecret = environmentVariableBo.get().getEnv(VernoSecrets.STRIPE_WEBHOOK_SECRET);

    final Event event;
    try {
      event = Webhook.constructEvent(
              payload,
              signatureHead,
              stripeWebhookSecret
      );
    } catch (SignatureVerificationException e) {
      throw new IllegalArgumentException("Could not construct webhook event", e);
    }

    if (billingWebhookEventService.get().isAlreadyProcessed(event.getId())) {
      return;
    }

    persistReceivedEvent(payload, event);

    try {
      switch (event.getType()) {
        case "checkout.session.completed" -> handleCheckoutSessionCompleted(event);
        case "invoice.paid", "invoice.payment_succeeded" -> handleInvoicePaid(event);
        case "invoice_payment.paid" -> handleInvoicePaymentPaid(event);
        case "invoice.payment_failed" -> handleInvoicePaymentFailed(event);
        case "customer.subscription.updated" -> handleSubscriptionUpdated(event);
        case "customer.subscription.deleted" -> handleSubscriptionDeleted(event);
        default -> {
        }
      }
      billingWebhookEventService.get().markProcessed(event.getId());
    } catch (Exception exception) {
      billingWebhookEventService.get().markFailed(event.getId(), exception.getMessage());
      throw exception;
    }
  }

  private void persistReceivedEvent(@Nonnull final String payload,
                                    @Nonnull final Event event) {
    final var existing = billingWebhookEventService.get().findByStripeEventId(event.getId());

    if (existing.isPresent()) {
      billingWebhookEventService.get().resetToReceived(event.getId());
      return;
    }

    final var dto = new BillingWebhookEventDto(
            event.getId(),
            event.getType(),
            BillingWebhookEventStatus.RECEIVED,
            null,
            OffsetDateTime.now(),
            payload
    );

    billingWebhookEventService.get().save(dto);
  }

  private void handleCheckoutSessionCompleted(@Nonnull final Event event) {
    final var stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);
    if (!(stripeObject instanceof Session session)) {
      return;
    }

    final var customerId = session.getCustomer();
    final var subscriptionId = session.getSubscription();
    if (customerId == null) {
      return;
    }

    final var billingOptional = resolveOrCreateTenantBilling(customerId, session.getMetadata());
    if (billingOptional.isEmpty()) {
      return;
    }

    final var billing = billingOptional.get();
    if (billing.getStripeCustomerId().isBlank()) {
      billing.setStripeCustomerId(customerId);
    }

    if (subscriptionId != null && !subscriptionId.isBlank()) {
      billing.setStripeSubscriptionId(subscriptionId);
    }

    tenantBillingService.get().save(billing);
  }

  private void handleInvoicePaid(@Nonnull final Event event) {
    final var stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);
    if (!(stripeObject instanceof Invoice invoice)) {
      return;
    }

    final var customerId = invoice.getCustomer();
    if (customerId == null) {
      return;
    }

    final var billingOptional = tenantBillingService.get().findOptionalByStripeCustomerId(customerId);
    if (billingOptional.isEmpty()) {
      return;
    }

    final var billing = billingOptional.get();
    if (billing.getStripeCustomerId().isBlank()) {
      billing.setStripeCustomerId(customerId);
    }

    billing.setPaymentStatus(BillingPaymentStatus.PAID);
    billing.setSubscriptionStatus(BillingSubscriptionStatus.ACTIVE);
    billing.setHasValidPaymentMethod(true);

    tenantBillingService.get().save(billing);
  }

  private void handleInvoicePaymentPaid(@Nonnull final Event event) {
    final var stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);
    if (!(stripeObject instanceof InvoicePayment invoicePayment)) {
      return;
    }

    final var invoiceId = invoicePayment.getInvoice();
    if (invoiceId == null) {
      return;
    }

    final var invoice = invoicePayment.getInvoiceObject();
    if (invoice == null) {
      return;
    }

    final var customerId = invoice.getCustomer();
    if (customerId == null) {
      return;
    }

    final var billingOptional = tenantBillingService.get().findOptionalByStripeCustomerId(customerId);
    if (billingOptional.isEmpty()) {
      return;
    }

    final var billing = billingOptional.get();

    billing.setPaymentStatus(BillingPaymentStatus.PAID);
    billing.setHasValidPaymentMethod(true);

    tenantBillingService.get().save(billing);
  }

  private void handleInvoicePaymentFailed(@Nonnull final Event event) {
    final var stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);
    if (!(stripeObject instanceof Invoice invoice)) {
      return;
    }

    final var customerId = invoice.getCustomer();
    if (customerId == null) {
      return;
    }

    final var billingOptional = tenantBillingService.get().findOptionalByStripeCustomerId(customerId);
    if (billingOptional.isEmpty()) {
      return;
    }

    final var billing = billingOptional.get();
    billing.setPaymentStatus(BillingPaymentStatus.FAILED);
    billing.setSubscriptionStatus(BillingSubscriptionStatus.PAST_DUE);

    tenantBillingService.get().save(billing);
  }

  private void handleSubscriptionUpdated(@Nonnull final Event event) {
    final var stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);
    if (!(stripeObject instanceof Subscription subscription)) {
      return;
    }

    final var customerId = subscription.getCustomer();
    if (customerId == null) {
      return;
    }

    final var billingOptional = tenantBillingService.get().findOptionalByStripeCustomerId(customerId);
    if (billingOptional.isEmpty()) {
      return;
    }

    final var billing = billingOptional.get();
    final var subscriptionId = subscription.getId();
    if (subscriptionId != null && !subscriptionId.isBlank()) {
      billing.setStripeSubscriptionId(subscriptionId);
      billing.setPlanKey(resolvePlanKey(subscription));
    }

    final var subscriptionStatus = BillingSubscriptionStatus.fromStripeStatus(subscription.getStatus());
    billing.setSubscriptionStatus(subscriptionStatus);

    tenantBillingService.get().save(billing);
  }

  @Nonnull
  private BillingPlanKey resolvePlanKey(@Nonnull final Subscription subscription) {
    final var items = subscription.getItems();
    if (items == null || items.getData() == null || items.getData().isEmpty()) {
      return BillingPlanKey.FREE;
    }
    final var price = items.getData().getFirst().getPrice();
    if (price == null || price.getId() == null) {
      return BillingPlanKey.FREE;
    }

    return BillingPlanResolver.resolve(price.getId(), environmentVariableBo.get());
  }

  private void handleSubscriptionDeleted(@Nonnull final Event event) {
    final var stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);
    if (!(stripeObject instanceof Subscription subscription)) {
      return;
    }

    final var customerId = subscription.getCustomer();
    if (customerId == null) {
      return;
    }

    final var billingOptional = tenantBillingService.get().findOptionalByStripeCustomerId(customerId);
    if (billingOptional.isEmpty()) {
      return;
    }

    final var billing = billingOptional.get();
    if (subscription.getId() != null && !subscription.getId().isBlank()) {
      billing.setStripeSubscriptionId(subscription.getId());
    }

    billing.setSubscriptionStatus(BillingSubscriptionStatus.CANCELED);

    tenantBillingService.get().save(billing);
  }

  @Nonnull
  private Optional<TenantBillingDto> resolveOrCreateTenantBilling(@Nonnull final String customerId,
                                                                  final Map<String, String> metadata) {
    final var existingBilling = tenantBillingService.get().findOptionalByStripeCustomerId(customerId);
    if (existingBilling.isPresent()) {
      return existingBilling;
    }

    if (metadata == null || metadata.isEmpty()) {
      return Optional.empty();
    }

    final var tenantIdRaw = metadata.get(VernoConstants.SESSION_TENANT_ID);
    if (tenantIdRaw == null || tenantIdRaw.isBlank()) {
      return Optional.empty();
    }

    final long tenantId;
    try {
      tenantId = Long.parseLong(tenantIdRaw);
    } catch (NumberFormatException exception) {
      return Optional.empty();
    }

    final var priceId = metadata.get(VernoConstants.SESSION_STRIPE_PRICE_ID);
    final var planKey = BillingPlanResolver.resolve(priceId, environmentVariableBo.get());

    final var newBilling = TenantBillingDto.empty();
    newBilling.setTenantId(tenantId);
    newBilling.setStripeCustomerId(customerId);
    newBilling.setPlanKey(planKey);
    newBilling.setSubscriptionStatus(BillingSubscriptionStatus.INACTIVE);
    newBilling.setPaymentStatus(BillingPaymentStatus.UNPAID);
    newBilling.setHasValidPaymentMethod(false);

    return Optional.of(tenantBillingService.get().save(newBilling));
  }
}
