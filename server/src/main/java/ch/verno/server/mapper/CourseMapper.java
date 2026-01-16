package ch.verno.server.mapper;

import ch.verno.common.db.dto.table.CourseDto;
import ch.verno.common.db.dto.table.CourseLevelDto;
import ch.verno.db.entity.CourseEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public final class CourseMapper {

  private CourseMapper() {
  }

  @Nonnull
  public static CourseDto toDto(@Nullable final CourseEntity entity) {
    if (entity == null) {
      return CourseDto.empty();
    }

    final var weekdays = entity.getWeekdays().isEmpty()
            ? new ArrayList<DayOfWeek>()
            : new ArrayList<>(entity.getWeekdays());

    final List<CourseLevelDto> courseLevels = entity.getCourseLevels() == null || entity.getCourseLevels().isEmpty()
            ? List.of()
            : entity.getCourseLevels().stream()
            .map(CourseLevelMapper::toDto)
            .toList();

    return new CourseDto(
            entity.getId(),
            entity.getTitle(),
            entity.getCapacity(),
            entity.getLocation(),
            courseLevels,
            CourseScheduleMapper.toDto(entity.getCourseSchedule()),
            weekdays,
            entity.getStartTime(),
            entity.getEndTime(),
            InstructorMapper.toDto(entity.getInstructor()),
            entity.getNote()
    );
  }

  @Nullable
  public static CourseEntity toEntity(@Nullable final CourseDto dto) {
    if (dto == null || dto.isEmpty()) {
      return null;
    }

    final var entity = new CourseEntity(
            dto.getTitle(),
            dto.getCapacity(),
            dto.getLocation(),
            CourseLevelMapper.toEntityRefs(dto.getCourseLevels()),
            CourseScheduleMapper.toEntity(dto.getCourseSchedule()),
            dto.getWeekdays(),
            dto.getStartTime(),
            dto.getEndTime(),
            InstructorMapper.toEntity(dto.getInstructor()),
            dto.getNote()
    );

    if (dto.getId() != null && dto.getId() != 0) {
      entity.setId(dto.getId());
    } else {
      entity.setId(null);
    }

    return entity;
  }

  @Nullable
  public static CourseEntity toEntityRef(@Nullable final CourseDto dto) {
    if (dto == null || dto.isEmpty() || dto.getId() == null || dto.getId() == 0) {
      return null;
    }

    return CourseEntity.ref(dto.getId());
  }
}