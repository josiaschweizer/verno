package ch.verno.common.db.dto.defaultdto;

import ch.verno.common.db.dto.table.MandantSettingDto;
import jakarta.annotation.Nonnull;

public class DefaultMandantSettingDto {

  private DefaultMandantSettingDto() {
  }

  public static MandantSettingDto getDefault(@Nonnull final Long mandantId) {
    return new MandantSettingDto(
            mandantId,
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
