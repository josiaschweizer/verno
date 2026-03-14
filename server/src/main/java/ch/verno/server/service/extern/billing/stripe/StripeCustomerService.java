package ch.verno.server.service.extern.billing.stripe;

import ch.verno.common.db.service.extern.ITenantBillingService;
import ch.verno.common.db.service.extern.billing.stripe.IStripeCustomerService;
import ch.verno.common.gate.GlobalInterface;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import jakarta.annotation.Nonnull;

public class StripeCustomerService implements IStripeCustomerService {

  public static final String TENANT_ID = "tenantId";
  public static final String USER_ID = "userId";
  @Nonnull private final ITenantBillingService tenantBillingService;

  public StripeCustomerService(@Nonnull final GlobalInterface globalInterface) {
    tenantBillingService = globalInterface.getService(ITenantBillingService.class);
  }

  @Nonnull
  @Override
  public String getOrCreateCustomer(@Nonnull final Long tenantId,
                                    @Nonnull final Long userId) {
    final var billingOptional = tenantBillingService.getOptionalTenantBillingByTenantId(tenantId);
    if (billingOptional.isPresent()) {

      final var billing = billingOptional.get();

      if (!billing.getStripeCustomerId().isBlank()) {
        return billing.getStripeCustomerId();
      }
    }

    try {
      final var params = CustomerCreateParams.builder()
              .putMetadata(TENANT_ID, String.valueOf(tenantId))
              .putMetadata(USER_ID, String.valueOf(userId))
              .build();

      final var customer = Customer.create(params);

      final var customerId = customer.getId();

      if (billingOptional.isPresent()) {

        final var billing = billingOptional.get();
        billing.setStripeCustomerId(customerId);
        tenantBillingService.createTenantBilling(billing);
      }

      return customerId;

    } catch (StripeException e) {
      throw new IllegalStateException("Failed to create Stripe customer", e);
    }
  }
}
