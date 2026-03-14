package ch.verno.server.service.extern.billing.token;

import ch.verno.common.db.type.billing.BillingAccessTokenPurpose;
import ch.verno.server.service.extern.billing.BaseBillingServiceTest;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TestBillingAccessTokenResolverService extends BaseBillingServiceTest {

  @Nonnull private BillingAccessTokenService billingAccessTokenService;
  @Nonnull private BillingAccessTokenGeneratorService generatorService;
  @Nonnull private BillingAccessTokenResolverService service;

  @BeforeEach
  void setUp() {
    billingAccessTokenService = Mockito.mock(BillingAccessTokenService.class);
    generatorService = Mockito.mock(BillingAccessTokenGeneratorService.class);
    service = new BillingAccessTokenResolverService(billingAccessTokenService, generatorService);
  }

  @Test
  @DisplayName("should resolve valid token")
  void resolveToken() {
    final var tokenDto = createValidBillingAccessTokenDto(TOKEN_HASH, BillingAccessTokenPurpose.UPDATE_PAYMENT_METHOD);

    Mockito.when(generatorService.hashToken(RAW_TOKEN)).thenReturn(TOKEN_HASH);
    Mockito.when(billingAccessTokenService.getBillingAccessTokenByTokenHash(TOKEN_HASH)).thenReturn(tokenDto);

    final var result = service.resolveBillingAccessToken(RAW_TOKEN);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(tokenDto, result);
  }

  @Test
  @DisplayName("should reject expired token")
  void resolveExpiredToken() {
    final var expiredDto = createExpiredBillingAccessTokenDto(TOKEN_HASH, BillingAccessTokenPurpose.UPDATE_PAYMENT_METHOD);

    Mockito.when(generatorService.hashToken(RAW_TOKEN)).thenReturn(TOKEN_HASH);
    Mockito.when(billingAccessTokenService.getBillingAccessTokenByTokenHash(TOKEN_HASH)).thenReturn(expiredDto);

    Assertions.assertThrows(
            IllegalStateException.class,
            () -> service.resolveBillingAccessToken(RAW_TOKEN)
    );
  }

  @Test
  @DisplayName("should reject used token")
  void resolveUsedToken() {
    final var usedDto = createUsedBillingAccessTokenDto(TOKEN_HASH, BillingAccessTokenPurpose.UPDATE_PAYMENT_METHOD);

    Mockito.when(generatorService.hashToken(RAW_TOKEN)).thenReturn(TOKEN_HASH);
    Mockito.when(billingAccessTokenService.getBillingAccessTokenByTokenHash(TOKEN_HASH)).thenReturn(usedDto);

    Assertions.assertThrows(
            IllegalStateException.class,
            () -> service.resolveBillingAccessToken(RAW_TOKEN)
    );
  }

  @Test
  @DisplayName("should reject token with wrong purpose")
  void resolveWrongPurpose() {
    final var tokenDto = createValidBillingAccessTokenDto(TOKEN_HASH, BillingAccessTokenPurpose.START_CHECKOUT);

    Mockito.when(generatorService.hashToken(RAW_TOKEN)).thenReturn(TOKEN_HASH);
    Mockito.when(billingAccessTokenService.getBillingAccessTokenByTokenHash(TOKEN_HASH)).thenReturn(tokenDto);

    Assertions.assertThrows(
            IllegalStateException.class,
            () -> service.resolveBillingAccessToken(
                    RAW_TOKEN,
                    BillingAccessTokenPurpose.UPDATE_PAYMENT_METHOD
            )
    );
  }

  @Test
  @DisplayName("should mark token as used")
  void resolveAndMarkUsed() {
    final var validDto = createValidBillingAccessTokenDto(TOKEN_HASH, BillingAccessTokenPurpose.OPEN_BILLING_PORTAL);
    final var usedDto = createUsedBillingAccessTokenDto(TOKEN_HASH, BillingAccessTokenPurpose.OPEN_BILLING_PORTAL);

    Mockito.when(generatorService.hashToken(RAW_TOKEN)).thenReturn(TOKEN_HASH);
    Mockito.when(billingAccessTokenService.getBillingAccessTokenByTokenHash(TOKEN_HASH)).thenReturn(validDto);
    Mockito.when(billingAccessTokenService.markBillingAccessTokenAsUsed(TOKEN_HASH)).thenReturn(usedDto);

    final var result = service.resolveAndMarkBillingAccessTokenAsUsed(RAW_TOKEN);

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.getUsedAt());
  }

  @Test
  @DisplayName("should return false when token is invalid")
  void invalidToken() {
    Mockito.when(generatorService.hashToken(RAW_TOKEN))
            .thenThrow(new IllegalStateException("invalid"));

    final var valid = service.isBillingAccessTokenValid(RAW_TOKEN);

    Assertions.assertFalse(valid);
  }

  @Test
  @DisplayName("should return true when token is valid")
  void validToken() {
    final var tokenDto = createValidBillingAccessTokenDto(TOKEN_HASH, BillingAccessTokenPurpose.UPDATE_PAYMENT_METHOD);

    Mockito.when(generatorService.hashToken(RAW_TOKEN)).thenReturn(TOKEN_HASH);
    Mockito.when(billingAccessTokenService.getBillingAccessTokenByTokenHash(TOKEN_HASH)).thenReturn(tokenDto);

    final var valid = service.isBillingAccessTokenValid(RAW_TOKEN);

    Assertions.assertTrue(valid);
  }
}