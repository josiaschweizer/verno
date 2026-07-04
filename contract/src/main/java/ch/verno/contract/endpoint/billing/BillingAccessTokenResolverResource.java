package ch.verno.contract.endpoint.billing;

import ch.verno.contract.dto.table.billing.BillingAccessTokenDto;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

@RpcEndpoint
public interface BillingAccessTokenResolverResource {

  @Nonnull
  BillingAccessTokenDto resolveBillingAccessToken(@Nonnull String rawToken);

}
