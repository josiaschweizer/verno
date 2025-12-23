package ch.verno.server.mapper;

import ch.verno.common.db.dto.CourseScheduleDto;
import ch.verno.db.entity.CourseScheduleEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public final class CourseScheduleMapper {
  private CourseScheduleMapper() {
  }

  @Nonnull
  public static CourseScheduleDto toDto(@Nullable final CourseScheduleEntity entity) {
    if (entity == null) {
      return CourseScheduleDto.empty();
    }

    return new CourseScheduleDto(
        entity.getId(),
        entity.getWeekStart() == null ? 0 : entity.getWeekStart(),
        entity.getWeekEnd() == null ? 0 : entity.getWeekEnd()
    );
  }

  @Nullable
  public static CourseScheduleEntity toEntity(@Nullable final CourseScheduleDto dto) {
    if (dto == null || dto.isEmpty()) {
      return null;
    }

    final var entity = new CourseScheduleEntity(
        dto.weekStart(),
        dto.weekEnd()
    );

    if (dto.id() != null) {
      entity.setId(dto.id());
    }

    return entity;
  }
}