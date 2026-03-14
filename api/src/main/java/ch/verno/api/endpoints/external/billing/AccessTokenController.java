package ch.verno.api.endpoints.external.billing;

import ch.verno.api.base.BaseController;
import ch.verno.publ.ApiUrl;
import ch.verno.server.service.extern.billing.token.BillingAccessTokenService;
import jakarta.annotation.Nonnull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.BILLING_ACCESS_TOKEN)
public class AccessTokenController extends BaseController {

  @Nonnull private final BillingAccessTokenService billingAccessTokenService;

  public AccessTokenController(@Nonnull final BillingAccessTokenService billingAccessTokenService) {
    this.billingAccessTokenService = billingAccessTokenService;
  }


}
