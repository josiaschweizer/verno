package ch.verno.common.db.service;

import ch.verno.common.db.dto.AppUserSettingDto;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface IAppUserSettingService {

  @Nonnull
  AppUserSettingDto createAppUserSetting(@Nonnull final AppUserSettingDto appUserSettingDto);

  @Nonnull
  AppUserSettingDto updateAppUserSetting(@Nonnull final AppUserSettingDto appUserSettingDto);

  @Nonnull
  AppUserSettingDto getAppUserSettingById(@Nonnull final Long id);

  @Nonnull
  AppUserSettingDto getAppUserSettingByUserId(@Nonnull Long id);

  @Nonnull
  List<AppUserSettingDto> getAppUserSettings();

  @Nonnull
  AppUserSettingDto saveAppUserSetting(@Nonnull AppUserSettingDto appUserSettingDto);
}
