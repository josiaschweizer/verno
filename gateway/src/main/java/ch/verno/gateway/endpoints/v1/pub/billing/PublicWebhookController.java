package ch.verno.gateway.endpoints.v1.pub.billing;

import ch.verno.contract.endpoint.billing.StripeResource;
import ch.verno.common.lib.api.ApiUrl;
import ch.verno.gateway.base.BaseController;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrl.PUBLIC_BILLING_WEBHOOK)
public class PublicWebhookController extends BaseController {

  @Nonnull private final StripeResource stripeResource;

  public PublicWebhookController(@Nonnull final RpcFactory rpcFactory) {
    this.stripeResource = rpcFactory.create(StripeResource.class);
  }


  @PostMapping(ApiUrl.STRIP_WEBHOOK)
  public ResponseEntity<String> createStripeWebhook(@RequestBody @Nonnull final String payload,
                                                    @RequestHeader("Stripe-Signature") @Nonnull final String signatureHeader) {



    stripeResource.handleStripeWebhook(payload, signatureHeader);
    return ResponseEntity.ok().build();
  }

}
