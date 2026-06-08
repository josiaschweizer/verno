package ch.verno.common.server.service.extern.billing.token;

import ch.verno.common.db.dto.billing.GeneratedBillingAccessTokenDto;
import ch.verno.common.db.type.billing.BillingAccessTokenPurpose;
import jakarta.annotation.Nonnull;

import java.time.OffsetDateTime;

public interface IBillingAccessTokenGeneratorService {

  @Nonnull
  GeneratedBillingAccessTokenDto generateBillingAccessToken(long tenantId,
                                                            long userId,
                                                            @Nonnull BillingAccessTokenPurpose purpose);

  @Nonnull
  GeneratedBillingAccessTokenDto generateBillingAccessToken(long tenantId,
                                                            long userId,
                                                            @Nonnull BillingAccessTokenPurpose purpose,
                                                            @Nonnull OffsetDateTime expiresAt);

  @Nonnull
  String hashToken(@Nonnull String rawToken);

  @Nonnull
  String generateRawToken();

}
