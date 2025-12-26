package ch.verno.server.mapper;

import ch.verno.common.db.dto.CourseLevelDto;
import ch.verno.common.util.Publ;
import ch.verno.db.entity.CourseLevelEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public final class CourseLevelMapper {
  private CourseLevelMapper() {
  }

  @Nonnull
  public static CourseLevelDto toDto(@Nullable final CourseLevelEntity entity) {
    if (entity == null) {
      return CourseLevelDto.empty();
    }

    return new CourseLevelDto(
        entity.getId(),
        entity.getCode() == null ? Publ.EMPTY_STRING : entity.getCode(),
        entity.getName() == null ? Publ.EMPTY_STRING : entity.getName(),
        entity.getDescription() == null ? Publ.EMPTY_STRING : entity.getDescription(),
        entity.getSortingOrder()
    );
  }

  @Nullable
  public static CourseLevelEntity toEntity(@Nullable final CourseLevelDto dto) {
    if (dto == null || dto.isEmpty()) {
      return null;
    }

    final var entity = new CourseLevelEntity(
        dto.getCode(),
        dto.getName(),
        dto.getDescription(),
        dto.getSortingOrder()
    );

    if (dto.getId() != null && dto.getId() != 0) {
      entity.setId(dto.getId());
    } else {
      entity.setId(null);
    }

    return entity;
  }

  @Nullable
  public static CourseLevelEntity toEntityRef(@Nullable final CourseLevelDto dto) {
    if (dto == null || dto.isEmpty() || dto.getId() == null || dto.getId() == 0) {
      return null;
    }

    return CourseLevelEntity.ref(dto.getId());
  }
}