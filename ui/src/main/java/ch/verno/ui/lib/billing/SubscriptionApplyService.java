package ch.verno.ui.lib.billing;

import ch.verno.common.server.service.extern.ITenantBillingService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubscriptionApplyService {

  @Nonnull private final GlobalInterface globalInterface;
  @Nonnull private final ITenantBillingService tenantBillingService;

  public SubscriptionApplyService(@Nonnull final GlobalInterface globalInterface) {
    this.globalInterface = globalInterface;
    this.tenantBillingService = globalInterface.getService(ITenantBillingService.class);
  }

  public void applyCurrentUserSubscriptionState() {
    final var currentUserOptional = globalInterface.getUserProperties().getOptionalCurrentUser();
    if (currentUserOptional.isEmpty()) {
      return;
    }

    final var appUser = currentUserOptional.get();
    final var tenantId = Optional.ofNullable(appUser.getTenantId()).orElse(Publ.ZERO_LONG);

    if (!tenantBillingService.hasTenantValidSubscription(tenantId)) {
      handleInvalidSubscription();
    }
  }

  private void handleInvalidSubscription() {
    final var dialog = new InvalidSubscriptionDialog(globalInterface);
    dialog.open();
  }
}