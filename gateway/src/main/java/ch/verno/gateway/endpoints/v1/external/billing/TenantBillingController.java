package ch.verno.gateway.endpoints.v1.external.billing;

import ch.verno.common.type.billing.BillingPaymentStatus;
import ch.verno.common.type.billing.BillingPlanKey;
import ch.verno.common.type.billing.BillingSubscriptionStatus;
import ch.verno.contract.api.exernal.billing.tenant.CreateTenantBillingRequest;
import ch.verno.contract.api.exernal.billing.tenant.CreateTenantBillingResponse;
import ch.verno.contract.dto.table.billing.TenantBillingDto;
import ch.verno.contract.endpoint.billing.BillingResource;
import ch.verno.contract.gateway.ApiUrl;
import ch.verno.gateway.base.BaseController;
import ch.verno.lib.Lazy;
import ch.verno.lib.Publ;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.BILLING)
public class TenantBillingController extends BaseController {

  @Nonnull private final Lazy<BillingResource> tenantBillingResource;

  public TenantBillingController(@Nonnull final RpcFactory rpcFactory) {
    this.tenantBillingResource = Lazy.of(() -> rpcFactory.create(BillingResource.class));
  }

  @Nonnull
  @PostMapping
  public CreateTenantBillingResponse create(@RequestBody @Nonnull final CreateTenantBillingRequest req) {
    final var dto = TenantBillingDto.empty();
    dto.setTenantId(req.tenantId());
    dto.setPlanKey(BillingPlanKey.valueOf(req.planKey()));
    dto.setSubscriptionStatus(BillingSubscriptionStatus.fromKey(req.subscriptionStatus()));
    dto.setPaymentStatus(BillingPaymentStatus.fromKey(req.paymentStatus()));
    dto.setHasValidPaymentMethod(req.hasValidPaymentMethod());

    final var saved = tenantBillingResource.get().createTenantBilling(dto);

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
