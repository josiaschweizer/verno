package ch.verno.server.mapper.billing;

import ch.verno.common.db.dto.table.billing.BillingAccessTokenDto;
import ch.verno.db.entity.billing.BillingAccessTokenEntity;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public final class BillingAccessTokenMapper {

  private BillingAccessTokenMapper() {
  }

  @Nonnull
  public static BillingAccessTokenDto toDto(@Nullable final BillingAccessTokenEntity entity) {
    if (entity == null) {
      return new BillingAccessTokenDto();
    }

    final var dto = new BillingAccessTokenDto(
            entity.getId(),
            entity.getUser() != null ? entity.getUser().getId() : null,
            entity.getTokenHash(),
            entity.getPurpose(),
            entity.getExpiresAt(),
            entity.getUsedAt(),
            entity.getCreatedAt()
    );

    if (entity.getTenant() != null) {
      dto.setTenantId(entity.getTenant().getId());
    }

    return dto;
  }

  @Nullable
  public static BillingAccessTokenEntity toEntity(@Nullable final BillingAccessTokenDto dto,
                                                  final long tenantId,
                                                  @Nonnull final AppUserEntity userRef) {
    if (dto == null) {
      return null;
    }

    final var entity = new BillingAccessTokenEntity(
            TenantEntity.ref(tenantId),
            userRef,
            dto.getTokenHash(),
            dto.getPurpose(),
            dto.getExpiresAt() != null ? dto.getExpiresAt() : java.time.OffsetDateTime.now()
    );

    if (dto.getId() != null && dto.getId() != 0) {
      entity.setId(dto.getId());
    }

    entity.setUsedAt(dto.getUsedAt());
    entity.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : java.time.OffsetDateTime.now());

    return entity;
  }

  public static void updateEntity(@Nonnull final BillingAccessTokenEntity entity,
                                  @Nonnull final BillingAccessTokenDto dto) {
    entity.setTokenHash(dto.getTokenHash());
    entity.setPurpose(dto.getPurpose());
    entity.setExpiresAt(dto.getExpiresAt() != null ? dto.getExpiresAt() : entity.getExpiresAt());
    entity.setUsedAt(dto.getUsedAt());
  }
}