package ch.verno.server.rpc.resource.billing;

import ch.verno.contract.endpoint.billing.StripeResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.billing.stripe.StripeBo;
import ch.verno.server.bo.billing.stripe.StripeWebhookBo;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
@RpcResource(StripeResource.class)
public class StripeResourceImpl implements StripeResource {

  @Nonnull private final Lazy<StripeBo> stripeBo;
  @Nonnull private final Lazy<StripeWebhookBo> stripeWebhookBo;

  public StripeResourceImpl(@Nonnull final ServerBean serverBean) {
    this.stripeBo = Lazy.of(() -> BoFactory.getInstance(serverBean).get(StripeBo.class));
    this.stripeWebhookBo = Lazy.of(() -> BoFactory.getInstance(serverBean).get(StripeWebhookBo.class));
  }

  @Nonnull
  @Override
  public String startBillingSession(@Nonnull final String token) {
    return stripeBo.get().startBillingSession(token);
  }

  @Override
  public void handleStripeWebhook(@Nonnull final String payload,
                                  @Nonnull final String signatureHead) {
    stripeWebhookBo.get().handleStripeWebhook(payload, signatureHead);
  }
}
