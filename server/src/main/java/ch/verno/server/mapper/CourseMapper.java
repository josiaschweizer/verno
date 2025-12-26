package ch.verno.server.mapper;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.util.Publ;
import ch.verno.db.entity.CourseEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public final class CourseMapper {

  private CourseMapper() {
  }

  @Nonnull
  public static CourseDto toDto(@Nullable final CourseEntity entity) {
    if (entity == null) {
      return CourseDto.empty();
    }

    return new CourseDto(
        entity.getId(),
        entity.getTitle() == null ? Publ.EMPTY_STRING : entity.getTitle(),
        entity.getCapacity() == null ? 0 : entity.getCapacity(),
        entity.getLocation() == null ? Publ.EMPTY_STRING : entity.getLocation(),
        CourseLevelMapper.toDto(entity.getLevel()),
        CourseScheduleMapper.toDto(entity.getSchedule()),
            entity.getWeekdays(),
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
        dto.getTitle(),
        dto.getCapacity(),
        dto.getLocation(),
            CourseLevelMapper.toEntityRef(dto.getCourseLevel()),
            CourseScheduleMapper.toEntity(dto.getCourseSchedule()),
        dto.getWeekdays(),
        dto.getDuration(),
        InstructorMapper.toEntity(dto.getInstructor())
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