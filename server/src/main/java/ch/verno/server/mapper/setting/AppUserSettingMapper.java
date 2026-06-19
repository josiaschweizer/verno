package ch.verno.server.mapper.setting;

import ch.verno.contract.dto.table.setting.AppUserSettingDto;
import ch.verno.db.entity.setting.AppUserSettingEntity;
import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.server.mapper.base.IEntityMapper;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class AppUserSettingMapper implements IEntityMapper<AppUserSettingEntity, AppUserSettingDto> {

  @Nonnull
  @Override
  public AppUserSettingDto toSimpleDto(@Nonnull final AppUserSettingEntity entity) {
    final var dto = AppUserSettingDto.empty();
    dto.setId(entity.getId());
    dto.setUserId(entity.getUser().getId());
    dto.setTheme(entity.getTheme());
    dto.setLanguageTag(entity.getLanguageTag());

    return dto;
  }

  @Nonnull
  @Override
  public AppUserSettingEntity toNewEntity(@Nonnull final AppUserSettingDto dto) {
    return new AppUserSettingEntity(
            AppUserEntity.ref(dto.getUserId()),
            dto.getTheme(),
            dto.getLanguageTag()
    );
  }

  @Override
  public void updateEntity(@Nonnull final AppUserSettingEntity entity,
                           @Nonnull final AppUserSettingDto dto) {
    entity.setUser(AppUserEntity.ref(dto.getUserId()));
    entity.setTheme(dto.getTheme());
    entity.setLanguageTag(dto.getLanguageTag());
  }
}