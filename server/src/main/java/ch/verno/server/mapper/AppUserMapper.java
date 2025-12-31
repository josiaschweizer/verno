package ch.verno.server.mapper;

import ch.verno.common.db.dto.AppUserDto;
import ch.verno.common.util.Publ;
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
            entity.getRole()
    );
  }

  @Nonnull
  public static AppUserEntity toEntity(@Nonnull final AppUserDto dto) {
    final var entity = new AppUserEntity(
            dto.getUsername(),
            Publ.EMPTY_STRING, // passwordHash bewusst NICHT im DTO → wird separat gesetzt
            dto.getRole()
    );

    entity.setId(dto.getId());
    return entity;
  }
}