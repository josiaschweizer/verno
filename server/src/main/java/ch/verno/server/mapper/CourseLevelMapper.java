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
        entity.getSortingOrder() == null ? 0 : entity.getSortingOrder()
    );
  }

  @Nullable
  public static CourseLevelEntity toEntity(@Nullable final CourseLevelDto dto) {
    if (dto == null || dto.isEmpty()) {
      return null;
    }

    final var entity = new CourseLevelEntity(
        dto.code(),
        dto.name(),
        dto.description(),
        dto.sortingOrder()
    );

    if (dto.id() != null) {
      entity.setId(dto.id());
    }

    return entity;
  }
}