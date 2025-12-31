package ch.verno.server.mapper;

import ch.verno.common.db.dto.AppUserSettingDto;
import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.db.entity.user.AppUserSettingEntity;
import jakarta.annotation.Nonnull;

public final class AppUserSettingMapper {

  private AppUserSettingMapper() {
  }

  public static AppUserSettingDto toDto(@Nonnull final AppUserSettingEntity entity) {
    return new AppUserSettingDto(
            entity.getId(),
            entity.getUser().getId(),
            entity.getTheme()
    );
  }

  public static AppUserSettingEntity toEntity(@Nonnull final AppUserSettingDto dto,
                                              @Nonnull final AppUserEntity user) {
    final var entity = new AppUserSettingEntity(user, dto.getTheme());
    entity.setId(dto.getId());
    return entity;
  }
}