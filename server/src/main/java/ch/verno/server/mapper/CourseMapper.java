package ch.verno.server.mapper;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.util.Publ;
import ch.verno.db.entity.CourseEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Set;

public final class CourseMapper {
  private CourseMapper() {
  }

  @Nonnull
  public static CourseDto toDto(@Nullable final CourseEntity entity) {
    if (entity == null) {
      return CourseDto.empty();
    }

    final var weekdays = entity.getWeekdays() == null ? Set.<java.time.DayOfWeek>of() : Set.copyOf(entity.getWeekdays());

    return new CourseDto(
        entity.getId(),
        entity.getTitle() == null ? Publ.EMPTY_STRING : entity.getTitle(),
        entity.getCapacity() == null ? 0 : entity.getCapacity(),
        entity.getLocation() == null ? Publ.EMPTY_STRING : entity.getLocation(),
        CourseScheduleMapper.toDto(entity.getSchedule()),
        weekdays,
        entity.getDuration() == null ? 0 : entity.getDuration(),
        InstructorMapper.toDto(entity.getInstructor())
    );
  }

  @Nullable
  public static CourseEntity toEntity(@Nullable final CourseDto dto) {
    if (dto == null || dto.isEmpty()) {
      return null;
    }

    final var entity = new CourseEntity(
        dto.title(),
        dto.capacity(),
        dto.location(),
        CourseScheduleMapper.toEntity(dto.schedule()),
        dto.weekdays(),
        dto.duration(),
        InstructorMapper.toEntity(dto.instructor())
    );

    if (dto.id() != null) {
      entity.setId(dto.id());
    }

    return entity;
  }
}