package ch.verno.server.mapper;

import ch.verno.common.db.dto.table.TenantDto;
import ch.verno.db.entity.tenant.TenantEntity;
import jakarta.annotation.Nonnull;

public final class TenantMapper {

  private TenantMapper() {
  }

  @Nonnull
  public static TenantDto toDto(@Nonnull final TenantEntity entity) {
    return new TenantDto(
            entity.getId(),
            entity.getSlug(),
            entity.getName()
    );
  }

  @Nonnull
  public static TenantEntity toEntity(@Nonnull final TenantDto dto) {
    final TenantEntity entity = TenantEntity.ref(dto.getId());
    entity.setSlug(dto.getSlug());
    entity.setName(dto.getName());
    return entity;
  }
}