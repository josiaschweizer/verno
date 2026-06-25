package ch.verno.ui.lib.billing;

import ch.verno.lib.Lazy;
import ch.verno.lib.Publ;
import ch.verno.rpc.client.billing.BillingClient;
import ch.verno.rpc.properties.user.UserProperties;
import com.google.inject.Inject;
import com.google.inject.Injector;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubscriptionApplyService {

  @Nonnull private final Injector injector;
  @Nonnull private final Lazy<UserProperties> userProperties;
  @Nonnull private final Lazy<BillingClient> tenantBillingClient;

  @Inject
  public SubscriptionApplyService(@Nonnull final Injector injector) {
    this.injector = injector;
    this.userProperties = Lazy.of(() -> injector.getInstance(UserProperties.class));
    this.tenantBillingClient = Lazy.of(() -> injector.getInstance(BillingClient.class));
  }

  public void applyCurrentUserSubscriptionState() {
    final var currentUserOptional = userProperties.get().getOptionalCurrentAppUser();
    if (currentUserOptional.isEmpty()) {
      return;
    }

    final var appUser = currentUserOptional.get();
    final var tenantId = Optional.ofNullable(appUser.getTenantId()).orElse(Publ.ZERO_LONG);

    if (!tenantBillingClient.get().hasValidSubscriptionByTenantId(tenantId)) {
      handleInvalidSubscription();
    }
  }

  private void handleInvalidSubscription() {
    final var dialog = injector.getInstance(InvalidSubscriptionDialog.class);
    dialog.open();
  }
}