package ch.verno.common.db.mapper;

import ch.verno.server.entity.GenderEntity;
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
