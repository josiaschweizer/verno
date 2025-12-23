package ch.verno.server.mapper;

import ch.verno.common.db.dto.GenderDto;
import ch.verno.common.util.Publ;
import ch.verno.db.entity.GenderEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public final class GenderMapper {
  private GenderMapper() {
  }

  @Nonnull
  public static GenderDto toDto(@Nullable final GenderEntity entity) {
    if (entity == null) {
      return GenderDto.empty();
    }

    return new GenderDto(
        entity.getId(),
        entity.getName() == null ? Publ.EMPTY_STRING : entity.getName(),
        entity.getDescription() == null ? Publ.EMPTY_STRING : entity.getDescription()
    );
  }

  @Nullable
  public static GenderEntity toEntity(@Nullable final GenderDto dto) {
    if (dto == null || dto.isEmpty()) {
      return null;
    }

    final GenderEntity entity = new GenderEntity(dto.name(), dto.description());

    if (dto.id() != null) {
      entity.setId(dto.id());
    }

    return entity;
  }
}