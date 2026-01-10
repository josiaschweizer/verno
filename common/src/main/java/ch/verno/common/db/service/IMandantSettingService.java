package ch.verno.common.db.service;

import ch.verno.common.db.dto.MandantSettingDto;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface IMandantSettingService {

  @Nonnull
  MandantSettingDto createMandanSetting(@Nonnull final MandantSettingDto mandantSettingDto);

  @Nonnull
  MandantSettingDto updateMandantSetting(@Nonnull final MandantSettingDto mandantSettingDto);

  @Nonnull
  MandantSettingDto getMandantSettingById(@Nonnull final Long id);

  @Nonnull
  List<MandantSettingDto> getAllMandantSettings();

  @Nonnull
  MandantSettingDto getSingleMandantSetting();

  @Nonnull
  MandantSettingDto getOrCreateSingleMandantSetting(@Nonnull MandantSettingDto defaultDto);

  @Nonnull
  MandantSettingDto saveSingleMandantSetting(@Nonnull MandantSettingDto dto);
}
