package ch.verno.api.endpoints.external.billing;

import ch.verno.api.base.BaseController;
import ch.verno.publ.ApiUrl;
import ch.verno.server.service.extern.TenantBillingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;

@RestController
@RequestMapping(ApiUrl.BILLING_WEBHOOK)
public class WebhookEventController extends BaseController {

  @Nonnull private final TenantBillingService tenantBillingService;

  public WebhookEventController(@Nonnull final TenantBillingService tenantBillingService) {
    this.tenantBillingService = tenantBillingService;
  }

}
