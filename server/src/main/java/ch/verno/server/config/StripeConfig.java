package ch.verno.server.config;

import ch.verno.common.gate.GlobalInterface;
import ch.verno.publ.Publ;
import ch.verno.publ.VernoSecrets;
import com.stripe.Stripe;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

  @Nonnull private final String stripeSecretKey;

  public StripeConfig(@Nonnull final GlobalInterface globalInterface) {
    stripeSecretKey = globalInterface.getEnvProperties().getEnvOrDefault(VernoSecrets.ENV_STRIPE_SECRET_KEY, Publ.EMPTY_STRING);
  }

  @PostConstruct
  private void init() {
    Stripe.apiKey = stripeSecretKey;
  }

}
