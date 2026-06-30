package ch.verno.gateway.endpoints.v1.external.billing;

import ch.verno.contract.api.exernal.billing.accesstoken.ResolveBillingAccessTokenRequest;
import ch.verno.contract.api.exernal.billing.accesstoken.ResolveBillingAccessTokenResponse;
import ch.verno.contract.endpoint.billing.BillingAccessTokenResolverResource;
import ch.verno.contract.gateway.ApiUrl;
import ch.verno.gateway.base.BaseController;
import ch.verno.lib.Publ;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(ApiUrl.BILLING_ACCESS_TOKEN)
public class AccessTokenController extends BaseController {

  @Nonnull private final BillingAccessTokenResolverResource tokenResolverResource;

  public AccessTokenController(@Nonnull final RpcFactory rpcFactory) {
    this.tokenResolverResource = rpcFactory.create(BillingAccessTokenResolverResource.class);
  }

  @Nonnull
  @PostMapping(ApiUrl.RESOLVE_ACCESS_TOKEN)
  public ResolveBillingAccessTokenResponse resolveEntryToken(@RequestBody @Nonnull final ResolveBillingAccessTokenRequest request) {
    final var resolvedToken = tokenResolverResource.resolveBillingAccessToken(request.token());

    return new ResolveBillingAccessTokenResponse(
            Optional.ofNullable(resolvedToken.getTenantId()).orElse(Publ.ZERO_LONG),
            Optional.ofNullable(resolvedToken.getUserId()).orElse(Publ.ZERO_LONG),
            resolvedToken.getPurpose(),
            resolvedToken.getExpiresAt()
    );
  }

}
