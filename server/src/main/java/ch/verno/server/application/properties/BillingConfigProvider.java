package ch.verno.server.application.properties;

import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConfigurationProperties(prefix = "verno.billing")
public class BillingConfigProvider {

  @Nullable private String subscriptionOverviewUrl;
  @Nullable private String checkoutCancelUrl;
  @Nullable private String checkoutSuccessUrl;
  @Nullable private String portalReturnUrl;

  @Nonnull
  public String getSubscriptionOverviewUrl() {
    return Optional.ofNullable(subscriptionOverviewUrl).orElse(Publ.EMPTY_STRING);
  }

  public void setSubscriptionOverviewUrl(@Nullable final String subscriptionOverviewUrl) {
    this.subscriptionOverviewUrl = subscriptionOverviewUrl;
  }

  @Nonnull
  public String getCheckoutCancelUrl() {
    return Optional.ofNullable(checkoutCancelUrl).orElse(Publ.EMPTY_STRING);
  }

  public void setCheckoutCancelUrl(@Nullable final String checkoutCancelUrl) {
    this.checkoutCancelUrl = checkoutCancelUrl;
  }

  @Nonnull
  public String getCheckoutSuccessUrl() {
    return Optional.ofNullable(checkoutSuccessUrl).orElse(Publ.EMPTY_STRING);
  }

  public void setCheckoutSuccessUrl(@Nullable final String checkoutSuccessUrl) {
    this.checkoutSuccessUrl = checkoutSuccessUrl;
  }

  @Nonnull
  public String getPortalReturnUrl() {
    return Optional.ofNullable(portalReturnUrl).orElse(Publ.EMPTY_STRING);
  }

  public void setPortalReturnUrl(@Nullable final String portalReturnUrl) {
    this.portalReturnUrl = portalReturnUrl;
  }
}
