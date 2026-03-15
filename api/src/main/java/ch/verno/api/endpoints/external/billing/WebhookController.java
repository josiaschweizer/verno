package ch.verno.api.endpoints.external.billing;

import ch.verno.api.base.BaseController;
import ch.verno.common.db.service.extern.billing.stripe.IStripeWebhookService;
import ch.verno.publ.ApiUrl;
import jakarta.annotation.Nonnull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrl.BILLING_WEBHOOK)
public class WebhookController extends BaseController {

  @Nonnull private final IStripeWebhookService stripeWebhookService;

  public WebhookController(@Nonnull final IStripeWebhookService stripeWebhookService) {
    this.stripeWebhookService = stripeWebhookService;
  }


  @PostMapping(ApiUrl.STRIP_WEBHOOK)
  public ResponseEntity<String> createStripeWebhook(@RequestBody @Nonnull final String payload,
                                                    @RequestHeader("Stripe-Signature") @Nonnull final String signatureHeader) {
    stripeWebhookService.handleStripeWebhook(payload, signatureHeader);
    return ResponseEntity.ok().build();
  }

}
