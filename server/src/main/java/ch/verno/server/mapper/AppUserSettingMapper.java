package ch.verno.server.mapper;

import ch.verno.common.db.dto.table.AppUserSettingDto;
import ch.verno.db.entity.mandant.MandantEntity;
import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.db.entity.user.AppUserSettingEntity;
import ch.verno.common.mandant.MandantContext;
import jakarta.annotation.Nonnull;

import java.util.Locale;

public final class AppUserSettingMapper {

  private AppUserSettingMapper() {
  }

  @Nonnull
  public static AppUserSettingDto toDto(@Nonnull final AppUserSettingEntity entity) {
    final var dto = new AppUserSettingDto(
            entity.getId(),
            entity.getUser().getId(),
            entity.getTheme(),
            Locale.of(entity.getLanguageTag())
    );

    if (entity.getUser() != null && entity.getUser().getMandant() != null) {
      dto.setMandantId(entity.getUser().getMandant().getId());
    }

    return dto;
  }

  @Nonnull
  public static AppUserSettingEntity toEntity(@Nonnull final AppUserSettingDto dto,
                                              @Nonnull final AppUserEntity user) {
    final var entity = new AppUserSettingEntity(MandantEntity.ref(MandantContext.getRequired()), user, dto.getTheme(), dto.getLanguageTag());
    entity.setId(dto.getId());
    return entity;
  }

  public static void updateEntity(@Nonnull final AppUserSettingEntity entity,
                                  @Nonnull final AppUserSettingDto dto) {
    entity.setTheme(dto.getTheme());
    entity.setLanguageTag(dto.getLanguageTag());
  }
}