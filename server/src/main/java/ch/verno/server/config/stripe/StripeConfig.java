package ch.verno.server.config.stripe;

import ch.verno.lib.Publ;
import ch.verno.lib.VernoSecrets;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.env.EnvironmentVariableBo;
import com.stripe.Stripe;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

  @Nonnull private final String stripeSecretKey;

  public StripeConfig(@Nonnull final ServerBean serverBean) {
    final var envBo = BoFactory.getInstance(serverBean).getEmptyConstructor(EnvironmentVariableBo.class);
    stripeSecretKey = envBo.getEnvOrDefault(VernoSecrets.ENV_STRIPE_SECRET_KEY, Publ.EMPTY_STRING);
  }

  @PostConstruct
  private void init() {
    Stripe.apiKey = stripeSecretKey;
  }

}
