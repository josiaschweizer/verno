package ch.verno.common.db.service;

import ch.verno.common.db.dto.table.TenantSettingDto;
import jakarta.annotation.Nonnull;

public interface ITenantSettingService {

  /**
   * Returns the settings of the current tenant (TenantContext).
   * If none exist yet, a default DTO is returned (without persisting it).
   */
  @Nonnull
  TenantSettingDto getCurrentTenantSettingOrDefault();

  /**
   * Returns the settings of the current tenant (TenantContext).
   * If none exist yet, they are created using the provided defaultDto.
   */
  @Nonnull
  TenantSettingDto getOrCreateCurrentTenantSetting(@Nonnull TenantSettingDto defaultDto);

  /**
   * Persists (upserts) the settings of the current tenant (TenantContext).
   * The tenant is taken exclusively from the context
   * (dto.tenantId / dto.id are ignored).
   */
  @Nonnull
  TenantSettingDto saveCurrentTenantSetting(@Nonnull TenantSettingDto dto);
}