package ch.verno.common.db.service;

import ch.verno.common.db.dto.table.MandantSettingDto;
import jakarta.annotation.Nonnull;

public interface IMandantSettingService {

  /**
   * Liefert die Settings des aktuellen Mandanten (MandantContext).
   * Falls noch keine existieren, wird ein Default-DTO zurückgegeben (ohne zu speichern).
   */
  @Nonnull
  MandantSettingDto getCurrentMandantSettingOrDefault();

  /**
   * Liefert die Settings des aktuellen Mandanten (MandantContext).
   * Falls noch keine existieren, werden sie mit defaultDto erstellt.
   */
  @Nonnull
  MandantSettingDto getOrCreateCurrentMandantSetting(@Nonnull MandantSettingDto defaultDto);

  /**
   * Speichert (upsert) die Settings des aktuellen Mandanten (MandantContext).
   * Der Mandant wird ausschliesslich aus dem Context genommen (dto.mandantId / dto.id werden ignoriert).
   */
  @Nonnull
  MandantSettingDto saveCurrentMandantSetting(@Nonnull MandantSettingDto dto);
}