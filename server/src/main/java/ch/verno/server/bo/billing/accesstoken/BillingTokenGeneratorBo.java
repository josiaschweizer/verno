package ch.verno.server.bo.billing.accesstoken;

import ch.verno.common.type.billing.BillingAccessTokenPurpose;
import ch.verno.contract.dto.lib.billing.GeneratedBillingAccessTokenDto;
import ch.verno.contract.dto.table.billing.BillingAccessTokenDto;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.entity.billing.BillingAccessTokenService;
import jakarta.annotation.Nonnull;

import java.time.OffsetDateTime;

public class BillingTokenGeneratorBo {

  private static final int DEFAULT_EXPIRY_MINUTES = 10;

  @Nonnull private final TokenHashUtil tokenHashUtil;

  @Nonnull private final Lazy<BillingAccessTokenService> billingAccessTokenService;

  protected BillingTokenGeneratorBo(@Nonnull final ServerBean serverBean) {
    this.tokenHashUtil = new TokenHashUtil();

    this.billingAccessTokenService = Lazy.of(() -> serverBean.get(BillingAccessTokenService.class));
  }

  @Nonnull
  public GeneratedBillingAccessTokenDto generateBillingAccessToken(@Nonnull final Long tenantId,
                                                                   @Nonnull final Long userId,
                                                                   @Nonnull final BillingAccessTokenPurpose purpose) {
    return generateBillingAccessToken(
            tenantId,
            userId,
            purpose,
            OffsetDateTime.now().plusMinutes(DEFAULT_EXPIRY_MINUTES)
    );
  }

  @Nonnull
  public GeneratedBillingAccessTokenDto generateBillingAccessToken(@Nonnull final Long tenantId,
                                                                   @Nonnull final Long userId,
                                                                   @Nonnull final BillingAccessTokenPurpose purpose,
                                                                   @Nonnull final OffsetDateTime expiresAt) {
    final var rawToken = tokenHashUtil.generateRawToken();
    final var tokenHash = tokenHashUtil.hashToken(rawToken);

    final var dto = BillingAccessTokenDto.create(tenantId, userId, tokenHash, purpose, expiresAt);
    final var savedToken = billingAccessTokenService.get().save(dto);

    return new GeneratedBillingAccessTokenDto(rawToken, savedToken);
  }


}
