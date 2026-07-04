package ch.verno.server.mapper.billing;

import ch.verno.contract.dto.table.billing.BillingAccessTokenDto;
import ch.verno.db.entity.billing.BillingAccessTokenEntity;
import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.server.mapper.base.AbstractEntityMapper;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;
@Component
public class BillingAccessTokenMapper extends AbstractEntityMapper<BillingAccessTokenEntity, BillingAccessTokenDto> {

  @Nonnull
  @Override
  public BillingAccessTokenDto toDto(@Nonnull final BillingAccessTokenEntity entity) {
    final var dto = BillingAccessTokenDto.empty();

    dto.setId(entity.getId());
    dto.setTenantId(entity.getTenant() != null ? entity.getTenant().getId() : null);
    dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
    dto.setTokenHash(entity.getTokenHash());
    dto.setPurpose(entity.getPurpose());
    dto.setExpiresAt(entity.getExpiresAt());
    dto.setUsedAt(entity.getUsedAt());
    dto.setCreatedAt(entity.getCreatedAt());

    return dto;
  }

  @Nonnull
  @Override
  public BillingAccessTokenEntity toNewEntity(@Nonnull final BillingAccessTokenDto dto) {
    if (dto.getUserId() == null) {
      throw new IllegalStateException("userId must not be null");
    }
    if (dto.getExpiresAt() == null) {
      throw new IllegalStateException("expiresAt must not be null");
    }

    return new BillingAccessTokenEntity(
            AppUserEntity.ref(dto.getUserId()),
            dto.getTokenHash(),
            dto.getPurpose(),
            dto.getExpiresAt()
    );
  }

  @Override
  public void updateEntity(@Nonnull final BillingAccessTokenEntity entity,
                           @Nonnull final BillingAccessTokenDto dto) {
    if (dto.getUserId() != null) {
      entity.setUser(AppUserEntity.ref(dto.getUserId()));
    }

    entity.setTokenHash(dto.getTokenHash());
    entity.setPurpose(dto.getPurpose());

    if (dto.getExpiresAt() != null) {
      entity.setExpiresAt(dto.getExpiresAt());
    }

    entity.setUsedAt(dto.getUsedAt());
  }
}