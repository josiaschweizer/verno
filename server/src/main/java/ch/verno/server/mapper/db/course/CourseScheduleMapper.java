package ch.verno.server.mapper.db.course;

import ch.verno.contract.dto.table.course.CourseScheduleDto;
import ch.verno.db.entity.course.CourseScheduleEntity;
import ch.verno.server.mapper.db.base.IEntityMapper;
import ch.verno.server.mapper.db.lib.YearWeekMapper;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class CourseScheduleMapper implements IEntityMapper<CourseScheduleEntity, CourseScheduleDto> {

  @Nonnull
  @Override
  public CourseScheduleDto toSimpleDto(@Nonnull final CourseScheduleEntity entity) {
    final var dto = CourseScheduleDto.empty();

    dto.setId(entity.getId());
    dto.setTenantId(entity.getTenant() != null ? entity.getTenant().getId() : null);
    dto.setTitle(entity.getTitle());
    dto.setColor(entity.getColor());
    dto.setStatus(entity.getStatus());
    dto.setWeeks(YearWeekMapper.mapWeeksToYearWeeks(entity.getWeeks()));

    return dto;
  }

  @Nonnull
  @Override
  public CourseScheduleEntity toNewEntity(@Nonnull final CourseScheduleDto dto) {
    final var entity = CourseScheduleEntity.empty();
    updateEntity(entity, dto);
    return entity;
  }

  @Override
  public void updateEntity(@Nonnull final CourseScheduleEntity entity,
                           @Nonnull final CourseScheduleDto dto) {
    entity.setTitle(dto.getTitle());
    entity.setColor(dto.getColor());
    entity.setStatus(dto.getStatus());
    entity.setWeeks(YearWeekMapper.mapWeeksToStrings(dto.getWeeks()));
  }
}