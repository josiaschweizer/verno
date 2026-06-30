package ch.verno.gateway.endpoints.v1.external.billing;

import ch.verno.contract.api.exernal.billing.session.StartBillingSessionRequest;
import ch.verno.contract.api.exernal.billing.session.StartBillingSessionResponse;
import ch.verno.contract.endpoint.billing.StripeResource;
import ch.verno.contract.gateway.ApiUrl;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.BILLING_SESSION)
public class StripeBillingSessionController {

  @Nonnull private final StripeResource stripeResource;

  public StripeBillingSessionController(@Nonnull final RpcFactory rpcFactory) {
    this.stripeResource = rpcFactory.create(StripeResource.class);
  }

  @Nonnull
  @PostMapping(ApiUrl.START_STRIPE_SESSION)
  public StartBillingSessionResponse startSession(@RequestBody @Nonnull final StartBillingSessionRequest request) {
    final var redirectUrl = stripeResource.startBillingSession(request.token());
    return new StartBillingSessionResponse(redirectUrl);
  }
}
