package ch.verno.contract.endpoint.billing;

import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

@RpcEndpoint
public interface StripeResource {

  @Nonnull
  String startBillingSession(@Nonnull String token);

  void handleStripeWebhook(@Nonnull String payload,
                           @Nonnull String signatureHead);
}
