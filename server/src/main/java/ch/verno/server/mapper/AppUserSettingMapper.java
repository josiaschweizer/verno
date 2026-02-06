package ch.verno.server.mapper;

import ch.verno.common.db.dto.table.AppUserSettingDto;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.db.entity.setting.AppUserSettingEntity;
import ch.verno.common.tenant.TenantContext;
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

    if (entity.getUser() != null && entity.getUser().getTenant() != null) {
      dto.setTenantId(entity.getUser().getTenant().getId());
    }

    return dto;
  }

  @Nonnull
  public static AppUserSettingEntity toEntity(@Nonnull final AppUserSettingDto dto,
                                              @Nonnull final AppUserEntity user) {
    final var entity = new AppUserSettingEntity(TenantEntity.ref(TenantContext.getRequired()), user, dto.getTheme(), dto.getLanguageTag());
    entity.setId(dto.getId());
    return entity;
  }

  public static void updateEntity(@Nonnull final AppUserSettingEntity entity,
                                  @Nonnull final AppUserSettingDto dto) {
    entity.setTheme(dto.getTheme());
    entity.setLanguageTag(dto.getLanguageTag());
  }
}