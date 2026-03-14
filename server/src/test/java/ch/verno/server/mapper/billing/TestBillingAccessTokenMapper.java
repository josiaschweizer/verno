package ch.verno.server.mapper.billing;

import ch.verno.common.db.dto.table.billing.BillingAccessTokenDto;
import ch.verno.common.db.type.billing.BillingAccessTokenPurpose;
import ch.verno.db.entity.billing.BillingAccessTokenEntity;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.server.mapper.BaseMapperTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TestBillingAccessTokenMapper extends BaseMapperTest {

  @Test
  @DisplayName("toDto should return empty dto when entity is null")
  void toDto() {
    final var result = BillingAccessTokenMapper.toDto(null);

    Assertions.assertNotNull(result);
    assertNullId(result.getId());
    Assertions.assertNull(result.getTenantId());
    Assertions.assertNull(result.getUserId());
    assertEmptyString(result.getTokenHash());
    assertEmptyString(result.getPurpose());
  }

  @Test
  @DisplayName("toEntity should map dto values to billing access token entity")
  void toEntity() {
    final var dto = new BillingAccessTokenDto();
    dto.setId(10L);
    dto.setTenantId(20L);
    dto.setUserId(30L);
    dto.setTokenHash("hash_123");
    dto.setPurpose(BillingAccessTokenPurpose.UPDATE_PAYMENT_METHOD.name());
    dto.setExpiresAt(EXPIRES_AT);
    dto.setUsedAt(UPDATED_AT);
    dto.setCreatedAt(CREATED_AT);

    final var userRef = Mockito.mock(AppUserEntity.class);

    final var entity = BillingAccessTokenMapper.toEntity(dto, 20L, userRef);

    Assertions.assertNotNull(entity);
    Assertions.assertEquals(10L, entity.getId());
    Assertions.assertEquals(20L, entity.getTenant().getId());
    Assertions.assertEquals("hash_123", entity.getTokenHash());
    Assertions.assertEquals(BillingAccessTokenPurpose.UPDATE_PAYMENT_METHOD.name(), entity.getPurpose());
    Assertions.assertEquals(EXPIRES_AT, entity.getExpiresAt());
    Assertions.assertEquals(UPDATED_AT, entity.getUsedAt());
    Assertions.assertEquals(CREATED_AT, entity.getCreatedAt());
  }

  @Test
  @DisplayName("updateEntity should overwrite mutable token fields from dto")
  void updateEntity() {
    final var user = Mockito.mock(AppUserEntity.class);

    final var entity = new BillingAccessTokenEntity(
            TenantEntity.ref(1L),
            user,
            "old_hash",
            BillingAccessTokenPurpose.START_CHECKOUT.name(),
            EXPIRES_AT
    );

    final var dto = new BillingAccessTokenDto();
    dto.setTokenHash("new_hash");
    dto.setPurpose(BillingAccessTokenPurpose.OPEN_BILLING_PORTAL.name());
    dto.setExpiresAt(UPDATED_AT);
    dto.setUsedAt(CREATED_AT);

    BillingAccessTokenMapper.updateEntity(entity, dto);

    Assertions.assertEquals("new_hash", entity.getTokenHash());
    Assertions.assertEquals(BillingAccessTokenPurpose.OPEN_BILLING_PORTAL.name(), entity.getPurpose());
    Assertions.assertEquals(UPDATED_AT, entity.getExpiresAt());
    Assertions.assertEquals(CREATED_AT, entity.getUsedAt());
  }
}