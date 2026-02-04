package ch.verno.server.mapper;

import ch.verno.common.db.dto.table.CourseScheduleDto;
import ch.verno.db.entity.CourseScheduleEntity;
import ch.verno.db.entity.mandant.MandantEntity;
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

    final var dto = new CourseScheduleDto(
            entity.getId(),
            entity.getCreatedAt(),
            entity.getTitle(),
            entity.getColor(),
            entity.getStatus(),
            weeks
    );

    // propagate mandant id
    if (entity.getMandant() != null) {
      dto.setMandantId(entity.getMandant().getId());
    }

    return dto;
  }

  @Nullable
  public static CourseScheduleEntity toEntity(@Nullable final CourseScheduleDto dto, final long mandantId) {
    if (dto == null || dto.isEmpty()) {
      return null;
    }

    final var weeks = YearWeekMapper.mapWeeksToStrings(dto.getWeeks());

    final var mandant = MandantEntity.ref(mandantId);

    final var entity = new CourseScheduleEntity(
            mandant,
            dto.getTitle(),
            dto.getColor(),
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