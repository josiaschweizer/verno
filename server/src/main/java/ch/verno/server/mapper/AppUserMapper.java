package ch.verno.server.mapper;

import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.common.db.role.Role;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.db.entity.user.AppUserEntity;
import jakarta.annotation.Nonnull;

public final class AppUserMapper {

  private AppUserMapper() {
  }

  @Nonnull
  public static AppUserDto toDto(@Nonnull final AppUserEntity entity) {
    final var dto = new AppUserDto(
            entity.getId(),
            entity.getUsername(),
            entity.getFirstname(),
            entity.getLastname(),
            entity.getEmail(),
            entity.getPasswordHash(),
            Role.fromString(entity.getRole()),
            entity.isActive()
    );

    if (entity.getTenant() != null) {
      dto.setTenantId(entity.getTenant().getId());
    }

    return dto;
  }

  @Nonnull
  public static AppUserEntity toEntity(@Nonnull final AppUserDto dto,
                                       @Nonnull final TenantEntity tenant) {
    final var entity = new AppUserEntity(
            tenant,
            dto.getUsername(),
            dto.getFirstname(),
            dto.getLastname(),
            dto.getEmail(),
            dto.getPasswordHash(),
            dto.getRole().getRole(),
            dto.isActive()
    );

    entity.setId(dto.getId());
    return entity;
  }
}