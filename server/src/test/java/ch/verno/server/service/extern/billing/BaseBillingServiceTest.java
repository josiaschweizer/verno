package ch.verno.server.service.extern.billing;

import ch.verno.common.db.dto.table.billing.BillingAccessTokenDto;
import ch.verno.common.db.type.billing.BillingAccessTokenPurpose;
import org.junit.jupiter.api.Assertions;

import java.time.OffsetDateTime;

public abstract class BaseBillingServiceTest {

  protected static final long TENANT_ID = 7777L;
  protected static final long USER_ID = 1001L;

  protected static final String RAW_TOKEN = "raw-token";
  protected static final String TOKEN_HASH = "hashed-token";

  protected OffsetDateTime now() {
    return OffsetDateTime.now();
  }

  protected OffsetDateTime futureTime() {
    return OffsetDateTime.now().plusMinutes(15);
  }

  protected OffsetDateTime pastTime() {
    return OffsetDateTime.now().minusMinutes(15);
  }

  protected BillingAccessTokenDto createValidBillingAccessTokenDto() {
    return createValidBillingAccessTokenDto(TOKEN_HASH, BillingAccessTokenPurpose.UPDATE_PAYMENT_METHOD);
  }

  protected BillingAccessTokenDto createValidBillingAccessTokenDto(final String tokenHash,
                                                                   final BillingAccessTokenPurpose purpose) {
    final var dto = new BillingAccessTokenDto();
    dto.setId(1L);
    dto.setTenantId(TENANT_ID);
    dto.setUserId(USER_ID);
    dto.setTokenHash(tokenHash);
    dto.setPurpose(purpose.name());
    dto.setExpiresAt(futureTime());
    dto.setUsedAt(null);
    dto.setCreatedAt(now());
    return dto;
  }

  protected BillingAccessTokenDto createExpiredBillingAccessTokenDto(final String tokenHash,
                                                                     final BillingAccessTokenPurpose purpose) {
    final var dto = createValidBillingAccessTokenDto(tokenHash, purpose);
    dto.setExpiresAt(pastTime());
    return dto;
  }

  protected BillingAccessTokenDto createUsedBillingAccessTokenDto(final String tokenHash,
                                                                  final BillingAccessTokenPurpose purpose) {
    final var dto = createValidBillingAccessTokenDto(tokenHash, purpose);
    dto.setUsedAt(now().minusMinutes(1));
    return dto;
  }

  protected void assertNotBlank(final String value) {
    Assertions.assertNotNull(value);
    Assertions.assertFalse(value.isBlank());
  }
}