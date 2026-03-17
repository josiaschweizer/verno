package ch.verno.api.endpoints.external.billing;

import ch.verno.api.base.BaseController;
import ch.verno.common.api.dto.exernal.billing.accesstoken.ResolveBillingAccessTokenRequest;
import ch.verno.common.api.dto.exernal.billing.accesstoken.ResolveBillingAccessTokenResponse;
import ch.verno.publ.ApiUrl;
import ch.verno.publ.Publ;
import ch.verno.server.service.extern.billing.token.BillingAccessTokenResolverService;
import jakarta.annotation.Nonnull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(ApiUrl.BILLING_ACCESS_TOKEN)
public class AccessTokenController extends BaseController {

  @Nonnull private final BillingAccessTokenResolverService billingAccessTokenResolverService;

  public AccessTokenController(@Nonnull final BillingAccessTokenResolverService billingAccessTokenResolverService) {
    this.billingAccessTokenResolverService = billingAccessTokenResolverService;
  }

  @Nonnull
  @PostMapping(ApiUrl.RESOLVE_ACCESS_TOKEN)
  public ResolveBillingAccessTokenResponse resolveEntryToken(@RequestBody @Nonnull final ResolveBillingAccessTokenRequest request) {
    final var resolvedToken = billingAccessTokenResolverService.resolveBillingAccessToken(request.token());

    return new ResolveBillingAccessTokenResponse(
            Optional.ofNullable(resolvedToken.getTenantId()).orElse(Publ.ZERO_LONG),
            Optional.ofNullable(resolvedToken.getUserId()).orElse(Publ.ZERO_LONG),
            resolvedToken.getPurpose(),
            resolvedToken.getExpiresAt()
    );
  }

}
