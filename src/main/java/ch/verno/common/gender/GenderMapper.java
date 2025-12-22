package ch.verno.common.gender;

import ch.verno.server.gender.entity.GenderEntity;
import ch.verno.ui.verno.participant.dto.GenderDto;
import jakarta.annotation.Nonnull;

public class GenderMapper {

  @Nonnull
  public static GenderDto toDto(@Nonnull final GenderEntity entity) {
    return new GenderDto(
        entity.getId(),
        entity.getName()
    );
  }

}
