package ch.verno.server.bo.billing;

import ch.verno.common.lib.url.UrlUtil;
import ch.verno.common.tenant.TenantContext;
import ch.verno.common.type.billing.BillingAccessTokenPurpose;
import ch.verno.common.type.billing.BillingLicenceOption;
import ch.verno.common.type.billing.BillingSubscriptionStatus;
import ch.verno.contract.dto.lib.billing.GeneratedBillingAccessTokenDto;
import ch.verno.contract.dto.table.billing.BillingAccessTokenDto;
import ch.verno.contract.dto.table.billing.TenantBillingDto;
import ch.verno.common.lib.api.ApiUrl;
import ch.verno.lib.Lazy;
import ch.verno.lib.New;
import ch.verno.server.application.properties.BillingConfigProvider;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.entity.billing.BillingAccessTokenService;
import ch.verno.server.service.entity.billing.TenantBillingService;
import ch.verno.server.util.security.TokenGenerator;
import jakarta.annotation.Nonnull;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public class BillingBo {

  private static final int DEFAULT_EXPIRY_MINUTES = 10;

  @Nonnull private final Lazy<TenantBillingService> tenantBillingService;
  @Nonnull private final Lazy<BillingConfigProvider> billingConfigProvider;
  @Nonnull private final Lazy<BillingAccessTokenService> billingAccessTokenService;

  protected BillingBo(@Nonnull final ServerBean bean) {
    this.tenantBillingService = Lazy.of(() -> bean.get(TenantBillingService.class));
    this.billingConfigProvider = Lazy.of(() -> bean.get(BillingConfigProvider.class));
    this.billingAccessTokenService = Lazy.of(() -> bean.get(BillingAccessTokenService.class));
  }

  public boolean isOptionLicenced(@Nonnull final BillingLicenceOption option) {
    return getTenantLicenceOptions().contains(option);
  }

  @Nonnull
  public List<BillingLicenceOption> getTenantLicenceOptions() {
    final var currentTenant = TenantContext.get();
    if (currentTenant == null) {
      return New.arrayList();
    }

    final var billing = tenantBillingService.get().findByTenantId(currentTenant);
    final var planOptions = billing.getPlanKey().getBillingLicenceOptions();
    final var additionalOptions = billing.getAdditionalLicenceOptions();

    return New.arrayList(planOptions, additionalOptions);
  }

  @Nonnull
  public TenantBillingDto getTenantBillingForCurrentTenant() {
    final var billing = getOptionalTenantBillingForCurrentTenant();
    if (billing.isEmpty()) {
      throw new IllegalStateException("No tenant billing found for current tenant");
    }

    return billing.get();
  }

  @Nonnull
  public Optional<TenantBillingDto> getOptionalTenantBillingForCurrentTenant() {
    final var currentTenant = TenantContext.get();
    if (currentTenant == null) {
      return Optional.empty();
    }

    return tenantBillingService.get().findOptionalByTenantId(currentTenant);
  }

  @Nonnull
  public String createSubscriptionUrlForCheckout(@Nonnull final Long userId) {
    final var tenantId = TenantContext.getRequired();

    final var tenantBilling = tenantBillingService.get().findOptionalByTenantId(tenantId);
    final var tokenPurpose = tenantBilling.isPresent() ?
            BillingAccessTokenPurpose.UPDATE_PAYMENT_METHOD :
            BillingAccessTokenPurpose.START_CHECKOUT;

    final var subscriptionOverviewUrl = billingConfigProvider.get().getSubscriptionOverviewUrl();
    final var accessToken = generateBillingAccessToken(
            tenantId,
            userId,
            tokenPurpose
    );

    return UrlUtil.buildUrl(subscriptionOverviewUrl, ApiUrl.ENTRY_TOKEN + accessToken.rawToken());
  }

  @Nonnull
  public GeneratedBillingAccessTokenDto generateBillingAccessToken(@Nonnull final Long tenantId,
                                                                   @Nonnull final Long userId,
                                                                   @Nonnull final BillingAccessTokenPurpose purpose) {
    return generateBillingAccessToken(tenantId, userId, purpose, OffsetDateTime.now().plusMinutes(DEFAULT_EXPIRY_MINUTES));
  }

  @Nonnull
  public GeneratedBillingAccessTokenDto generateBillingAccessToken(@Nonnull final Long tenantId,
                                                                   @Nonnull final Long userId,
                                                                   @Nonnull final BillingAccessTokenPurpose purpose,
                                                                   @Nonnull final OffsetDateTime expiresAt) {
    final var tokenGenerator = new TokenGenerator();
    final var rawToken = tokenGenerator.generateRawToken();
    final var tokenHash = tokenGenerator.hashToken(rawToken);

    final var dto = BillingAccessTokenDto.empty();
    dto.setTenantId(tenantId);
    dto.setUserId(userId);
    dto.setTokenHash(tokenHash);
    dto.setPurpose(purpose.name());
    dto.setExpiresAt(expiresAt);

    final var savedToken = billingAccessTokenService.get().save(dto);
    return new GeneratedBillingAccessTokenDto(rawToken, savedToken);
  }

  public boolean isSubscriptionValid(@Nonnull final TenantBillingDto billing) {
    if (BillingSubscriptionStatus.ACTIVE.equals(billing.getSubscriptionStatus())) {
      return true;
    } else if (BillingSubscriptionStatus.TRIAL.equals(billing.getSubscriptionStatus())) {
      return true;
    } else if (BillingSubscriptionStatus.PAST_DUE.equals(billing.getSubscriptionStatus())) {
      return billing.getGraceUntil() != null && billing.getGraceUntil().isAfter(OffsetDateTime.now());
    }

    return false;
  }
}
