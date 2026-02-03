package ch.verno.server.mapper;

import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.db.entity.mandant.MandantEntity;
import jakarta.annotation.Nonnull;

public final class AppUserMapper {

  private AppUserMapper() {
  }

  @Nonnull
  public static AppUserDto toDto(@Nonnull final AppUserEntity entity) {
    final var dto = new AppUserDto(
            entity.getId(),
            entity.getUsername(),
            entity.getPasswordHash(),
            entity.getRole()
    );

    if (entity.getMandant() != null) {
      dto.setMandantId(entity.getMandant().getId());
    }

    return dto;
  }

  @Nonnull
  public static AppUserEntity toEntity(@Nonnull final AppUserDto dto, final long mandantId) {
    final var entity = new AppUserEntity(
            MandantEntity.ref(mandantId),
            dto.getUsername(),
            dto.getPasswordHash(),
            dto.getRole()
    );

    entity.setId(dto.getId());
    return entity;
  }
}