package ch.verno.api.endpoints.external.billing;

import ch.verno.api.base.BaseController;
import ch.verno.common.api.dto.exernal.billing.tenant.CreateTenantBillingRequest;
import ch.verno.common.api.dto.exernal.billing.tenant.CreateTenantBillingResponse;
import ch.verno.common.db.dto.table.billing.TenantBillingDto;
import ch.verno.common.db.type.billing.BillingPaymentStatus;
import ch.verno.common.db.type.billing.BillingPlanKey;
import ch.verno.common.db.type.billing.BillingSubscriptionStatus;
import ch.verno.publ.ApiUrl;
import ch.verno.publ.Publ;
import ch.verno.server.service.extern.billing.TenantBillingService;
import jakarta.annotation.Nonnull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.BILLING)
public class TenantBillingController extends BaseController {

  @Nonnull private final TenantBillingService tenantBillingService;

  public TenantBillingController(@Nonnull final TenantBillingService tenantBillingService) {
    this.tenantBillingService = tenantBillingService;
  }

  @Nonnull
  @PostMapping
  public CreateTenantBillingResponse create(@RequestBody @Nonnull final CreateTenantBillingRequest req) {
    final var dto = new TenantBillingDto();
    dto.setTenantId(req.tenantId());
    dto.setPlanKey(BillingPlanKey.valueOf(req.planKey()));
    dto.setSubscriptionStatus(BillingSubscriptionStatus.fromKey(req.subscriptionStatus()));
    dto.setPaymentStatus(BillingPaymentStatus.fromKey(req.paymentStatus()));
    dto.setHasValidPaymentMethod(req.hasValidPaymentMethod());

    final var saved = tenantBillingService.createTenantBilling(dto);

    return new CreateTenantBillingResponse(
            saved.getId() != null ? saved.getId() : Publ.ZERO_LONG,
            saved.getTenantId() != null ? saved.getTenantId() : Publ.ZERO_LONG,
            saved.getPlanKey().name(),
            saved.getSubscriptionStatus().getKey(),
            saved.getPaymentStatus().getKey(),
            saved.isHasValidPaymentMethod(),
            saved.getCurrentPeriodEnd(),
            saved.getGraceUntil()
    );
  }
}
