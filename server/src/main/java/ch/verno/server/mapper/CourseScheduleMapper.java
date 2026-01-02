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

    final var weeks = YearWeekMapper.mapWeeksToYearWeeks(entity.getWeeks());

    return new CourseScheduleDto(
            entity.getId(),
            entity.getCreatedAt(),
            entity.getTitle(),
            entity.getStatus(),
            weeks
    );
  }

  @Nullable
  public static CourseScheduleEntity toEntity(@Nullable final CourseScheduleDto dto) {
    if (dto == null || dto.isEmpty()) {
      return null;
    }

    final var weeks = YearWeekMapper.mapWeeksToStrings(dto.getWeeks());

    final var entity = new CourseScheduleEntity(
            dto.getTitle(),
            dto.getStatus(),
            weeks
    );

    if (dto.getId() != null && dto.getId() != 0L) {
      entity.setId(dto.getId());
    } else {
      entity.setId(null);
    }

    return entity;
  }
}