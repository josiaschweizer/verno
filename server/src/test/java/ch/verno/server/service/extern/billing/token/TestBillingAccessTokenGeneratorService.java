package ch.verno.server.service.extern.billing.token;

import ch.verno.common.db.dto.table.billing.BillingAccessTokenDto;
import ch.verno.common.db.type.billing.BillingAccessTokenPurpose;
import ch.verno.server.service.extern.billing.BaseBillingServiceTest;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class TestBillingAccessTokenGeneratorService extends BaseBillingServiceTest {

  @Nonnull private BillingAccessTokenService billingAccessTokenService;
  @Nonnull private BillingAccessTokenGeneratorService service;

  @BeforeEach
  void setUp() {
    billingAccessTokenService = Mockito.mock(BillingAccessTokenService.class);
    service = new BillingAccessTokenGeneratorService(billingAccessTokenService);
  }

  @Test
  @DisplayName("should generate raw token and persist hashed token")
  void generateToken() {
    final var savedDto = createValidBillingAccessTokenDto(TOKEN_HASH, BillingAccessTokenPurpose.UPDATE_PAYMENT_METHOD);
    Mockito.when(billingAccessTokenService.createBillingAccessToken(Mockito.any(BillingAccessTokenDto.class)))
            .thenReturn(savedDto);

    final var result = service.generateBillingAccessToken(
            TENANT_ID,
            USER_ID,
            BillingAccessTokenPurpose.UPDATE_PAYMENT_METHOD
    );

    Assertions.assertNotNull(result);
    assertNotBlank(result.rawToken());
    Assertions.assertEquals(savedDto, result.billingAccessToken());

    final var captor = ArgumentCaptor.forClass(BillingAccessTokenDto.class);
    Mockito.verify(billingAccessTokenService).createBillingAccessToken(captor.capture());

    final var persistedDto = captor.getValue();
    Assertions.assertEquals(TENANT_ID, persistedDto.getTenantId());
    Assertions.assertEquals(USER_ID, persistedDto.getUserId());
    Assertions.assertEquals(BillingAccessTokenPurpose.UPDATE_PAYMENT_METHOD.name(), persistedDto.getPurpose());

    Assertions.assertNotNull(persistedDto.getExpiresAt());
    assertNotBlank(persistedDto.getTokenHash());
    Assertions.assertNotEquals(result.rawToken(), persistedDto.getTokenHash());
  }

  @Test
  @DisplayName("should use provided expiration timestamp")
  void generateTokenWithExpiry() {
    final var customExpiry = futureTime().plusMinutes(30);
    final var savedDto = createValidBillingAccessTokenDto(TOKEN_HASH, BillingAccessTokenPurpose.START_CHECKOUT);
    savedDto.setExpiresAt(customExpiry);

    Mockito.when(billingAccessTokenService.createBillingAccessToken(Mockito.any(BillingAccessTokenDto.class)))
            .thenReturn(savedDto);

    final var result = service.generateBillingAccessToken(
            TENANT_ID,
            USER_ID,
            BillingAccessTokenPurpose.START_CHECKOUT,
            customExpiry
    );

    Assertions.assertNotNull(result);
    Assertions.assertEquals(customExpiry, result.getExpiresAt());

    final var captor = ArgumentCaptor.forClass(BillingAccessTokenDto.class);
    Mockito.verify(billingAccessTokenService).createBillingAccessToken(captor.capture());

    final var persistedDto = captor.getValue();
    Assertions.assertEquals(customExpiry, persistedDto.getExpiresAt());
    Assertions.assertEquals(BillingAccessTokenPurpose.START_CHECKOUT.name(), persistedDto.getPurpose());
  }

  @Test
  @DisplayName("should hash token deterministically")
  void hashToken() {
    final var rawToken = "test-token";

    final var firstHash = service.hashToken(rawToken);
    final var secondHash = service.hashToken(rawToken);

    assertNotBlank(firstHash);
    Assertions.assertEquals(firstHash, secondHash);
  }
}