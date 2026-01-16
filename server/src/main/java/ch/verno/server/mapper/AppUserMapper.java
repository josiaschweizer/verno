package ch.verno.server.mapper;

import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.db.entity.user.AppUserEntity;
import jakarta.annotation.Nonnull;

public final class AppUserMapper {

  private AppUserMapper() {
  }

  @Nonnull
  public static AppUserDto toDto(@Nonnull final AppUserEntity entity) {
    return new AppUserDto(
            entity.getId(),
            entity.getUsername(),
            entity.getPasswordHash(),
            entity.getRole()
    );
  }

  @Nonnull
  public static AppUserEntity toEntity(@Nonnull final AppUserDto dto) {
    final var entity = new AppUserEntity(
            dto.getUsername(),
            dto.getPasswordHash(),
            dto.getRole()
    );

    entity.setId(dto.getId());
    return entity;
  }
}