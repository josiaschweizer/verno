package ch.verno.api.endpoints.external.billing;

import ch.verno.common.api.dto.exernal.billing.session.StartBillingSessionRequest;
import ch.verno.common.api.dto.exernal.billing.session.StartBillingSessionResponse;
import ch.verno.publ.ApiUrl;
import ch.verno.server.service.extern.billing.session.StripeBillingSessionService;
import jakarta.annotation.Nonnull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.BILLING_SESSION)
public class StripeBillingSessionController {

  @Nonnull private final StripeBillingSessionService sessionService;

  public StripeBillingSessionController(@Nonnull final StripeBillingSessionService sessionService) {
    this.sessionService = sessionService;
  }

  @Nonnull
  @PostMapping(ApiUrl.START_STRIPE_SESSION)
  public StartBillingSessionResponse startSession(@RequestBody @Nonnull final StartBillingSessionRequest request) {
    final var redirectUrl = sessionService.startBillingSession(request.token());
    return new StartBillingSessionResponse(redirectUrl);
  }
}
