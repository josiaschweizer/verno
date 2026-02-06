package ch.verno.common.db.dto.defaultdto;

import ch.verno.common.db.dto.table.TenantSettingDto;
import jakarta.annotation.Nonnull;

public class DefaultTenantSettingDto {

  private DefaultTenantSettingDto() {
  }

  public static TenantSettingDto getDefault(@Nonnull final Long tenantId) {
    return new TenantSettingDto(
            tenantId,
            8,
            12,
            false,
            false,
            true,
            "Course Report",
            false
    );
  }

}
