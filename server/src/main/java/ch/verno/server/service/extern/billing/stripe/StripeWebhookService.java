package ch.verno.server.service.extern.billing.stripe;

import ch.verno.common.db.dto.table.billing.BillingWebhookEventDto;
import ch.verno.common.db.dto.table.billing.TenantBillingDto;
import ch.verno.common.db.service.extern.ITenantBillingService;
import ch.verno.common.db.service.extern.billing.IBillingWebhookEventService;
import ch.verno.common.db.service.extern.billing.stripe.IStripeWebhookService;
import ch.verno.common.db.type.billing.BillingPaymentStatus;
import ch.verno.common.db.type.billing.BillingPlanKey;
import ch.verno.common.db.type.billing.BillingSubscriptionStatus;
import ch.verno.common.db.type.billing.BillingWebhookEventStatus;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.publ.Publ;
import ch.verno.publ.VernoConstants;
import ch.verno.publ.VernoSecrets;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class StripeWebhookService implements IStripeWebhookService {

  @Nonnull private final GlobalInterface globalInterface;
  @Nonnull private final ITenantBillingService tenantBillingService;
  @Nonnull private final IBillingWebhookEventService billingWebhookEventService;

  public StripeWebhookService(@Nonnull final GlobalInterface globalInterface) {
    this.globalInterface = globalInterface;

    tenantBillingService = globalInterface.getService(ITenantBillingService.class);
    billingWebhookEventService = globalInterface.getService(IBillingWebhookEventService.class);
  }

  @Override
  public void handleStripeWebhook(@Nonnull final String payload,
                                  @Nonnull final String signatureHeader) {
    final var stripeWebhookSecret = globalInterface.getEnvProperties()
            .getEnvOrDefault(VernoSecrets.STRIPE_WEBHOOK_SECRET, Publ.EMPTY_STRING);

    final Event event;
    try {
      event = Webhook.constructEvent(
              payload,
              signatureHeader,
              stripeWebhookSecret
      );
    } catch (SignatureVerificationException exception) {
      throw new IllegalArgumentException("Could not construct webhook event", exception);
    }

    persistReceivedEvent(payload, event);

    System.out.println(event.getType());
    switch (event.getType()) {
      case "checkout.session.completed" -> handleCheckoutSessionCompleted(event);
      case "invoice.paid" -> handleInvoicePaid(event);
      case "invoice.payment_failed" -> handleInvoicePaymentFailed(event);
      case "customer.subscription.updated" -> handleSubscriptionUpdated(event);
      case "customer.subscription.deleted" -> handleSubscriptionDeleted(event);
      default -> {
        // ignore for now
      }
    }

    billingWebhookEventService.markProcessed(event.getId());
  }

  private void persistReceivedEvent(@Nonnull final String payload,
                                    @Nonnull final Event event) {
    final var dto = new BillingWebhookEventDto(
            event.getId(),
            event.getType(),
            BillingWebhookEventStatus.RECEIVED,
            null,
            OffsetDateTime.now(),
            payload
    );

    billingWebhookEventService.createBillingWebhookEvent(dto);
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

    tenantBillingService.saveTenantBilling(billing);
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

    final var billingOptional = tenantBillingService.getOptionalTenantBillingByStripeCustomerId(customerId);
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

    tenantBillingService.saveTenantBilling(billing);
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

    final var billingOptional = tenantBillingService.getOptionalTenantBillingByStripeCustomerId(customerId);
    if (billingOptional.isEmpty()) {
      return;
    }

    final var billing = billingOptional.get();
    billing.setPaymentStatus(BillingPaymentStatus.FAILED);
    billing.setSubscriptionStatus(BillingSubscriptionStatus.PAST_DUE);

    tenantBillingService.saveTenantBilling(billing);
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

    final var billingOptional = tenantBillingService.getOptionalTenantBillingByStripeCustomerId(customerId);
    if (billingOptional.isEmpty()) {
      return;
    }

    final var billing = billingOptional.get();
    final var subscriptionId = subscription.getId();
    if (subscriptionId != null && !subscriptionId.isBlank()) {
      final var planKey = BillingPlanKey.resolvePlan(subscriptionId, globalInterface);

      billing.setStripeSubscriptionId(subscriptionId);
      billing.setPlanKey(planKey);
    }

    final var subscriptionStatus = BillingSubscriptionStatus.fromStripeStatus(subscription.getStatus());
    billing.setSubscriptionStatus(subscriptionStatus);

    tenantBillingService.saveTenantBilling(billing);
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

    final var billingOptional = tenantBillingService.getOptionalTenantBillingByStripeCustomerId(customerId);
    if (billingOptional.isEmpty()) {
      return;
    }

    final var billing = billingOptional.get();

    if (subscription.getId() != null && !subscription.getId().isBlank()) {
      billing.setStripeSubscriptionId(subscription.getId());
    }

    billing.setSubscriptionStatus(BillingSubscriptionStatus.CANCELED);

    tenantBillingService.saveTenantBilling(billing);
  }

  @Nonnull
  private Optional<TenantBillingDto> resolveOrCreateTenantBilling(@Nonnull final String customerId,
                                                                  final Map<String, String> metadata) {
    final var existingBilling = tenantBillingService.getOptionalTenantBillingByStripeCustomerId(customerId);
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

    final var subscriptionId = metadata.get(VernoConstants.SESSION_STRIPE_PRICE_ID);
    final var planKey = BillingPlanKey.resolvePlan(subscriptionId, globalInterface);

    final var newBilling = new TenantBillingDto();
    newBilling.setTenantId(tenantId);
    newBilling.setStripeCustomerId(customerId);
    newBilling.setPlanKey(planKey);
    newBilling.setSubscriptionStatus(BillingSubscriptionStatus.INACTIVE);
    newBilling.setPaymentStatus(BillingPaymentStatus.UNPAID);
    newBilling.setHasValidPaymentMethod(false);

    final var savedBilling = tenantBillingService.createTenantBilling(newBilling);
    return Optional.of(savedBilling);
  }
}