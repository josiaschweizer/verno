package ch.verno.server.rpc.resource.billing;

import ch.verno.contract.dto.table.billing.BillingAccessTokenDto;
import ch.verno.contract.endpoint.billing.BillingAccessTokenResolverResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.billing.accesstoken.BillingTokenResolverBo;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
@RpcResource(BillingAccessTokenResolverResource.class)
public class BillingAccessTokenResolverResourceImpl implements BillingAccessTokenResolverResource {

  @Nonnull private final Lazy<BillingTokenResolverBo> billingResolverBo;

  public BillingAccessTokenResolverResourceImpl(@Nonnull final ServerBean serverBean) {
    this.billingResolverBo = Lazy.of(() -> BoFactory.getInstance(serverBean).get(BillingTokenResolverBo.class));
  }

  @Nonnull
  @Override
  public BillingAccessTokenDto resolveBillingAccessToken(@Nonnull final String rawToken) {
    return billingResolverBo.get().resolveBillingAccessToken(rawToken);
  }

}
