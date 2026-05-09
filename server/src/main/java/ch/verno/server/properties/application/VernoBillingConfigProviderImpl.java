package ch.verno.server.properties.application;

import ch.verno.common.properties.configprovider.VernoBillingConfigProvider;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConfigurationProperties(prefix = "verno.billing")
public class VernoBillingConfigProviderImpl implements VernoBillingConfigProvider {

  @Nullable private String subscriptionOverviewUrl;
  @Nullable private String checkoutCancelUrl;
  @Nullable private String checkoutSuccessUrl;
  @Nullable private String portalReturnUrl;

  @Nonnull
  @Override
  public String getSubscriptionOverviewUrl() {
    return Optional.ofNullable(subscriptionOverviewUrl).orElse(Publ.EMPTY_STRING);
  }

  public void setSubscriptionOverviewUrl(@Nullable final String subscriptionOverviewUrl) {
    this.subscriptionOverviewUrl = subscriptionOverviewUrl;
  }

  @Nonnull
  @Override
  public String getCheckoutCancelUrl() {
    return Optional.ofNullable(checkoutCancelUrl).orElse(Publ.EMPTY_STRING);
  }

  public void setCheckoutCancelUrl(@Nullable final String checkoutCancelUrl) {
    this.checkoutCancelUrl = checkoutCancelUrl;
  }

  @Nonnull
  @Override
  public String getCheckoutSuccessUrl() {
    return Optional.ofNullable(checkoutSuccessUrl).orElse(Publ.EMPTY_STRING);
  }

  public void setCheckoutSuccessUrl(@Nullable final String checkoutSuccessUrl) {
    this.checkoutSuccessUrl = checkoutSuccessUrl;
  }

  @Nonnull
  @Override
  public String getPortalReturnUrl() {
    return Optional.ofNullable(portalReturnUrl).orElse(Publ.EMPTY_STRING);
  }

  public void setPortalReturnUrl(@Nullable final String portalReturnUrl) {
    this.portalReturnUrl = portalReturnUrl;
  }
}
