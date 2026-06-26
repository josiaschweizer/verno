package ch.verno.server.mapper.course;

import ch.verno.contract.dto.table.course.CourseLevelDto;
import ch.verno.db.entity.course.CourseLevelEntity;
import ch.verno.server.mapper.base.IEntityMapper;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class CourseLevelMapper implements IEntityMapper<CourseLevelEntity, CourseLevelDto> {

  @Nonnull
  @Override
  public CourseLevelDto toSimpleDto(@Nonnull final CourseLevelEntity entity) {
    final var dto = CourseLevelDto.empty();

    dto.setId(entity.getId());
    dto.setTenantId(entity.getTenant() != null ? entity.getTenant().getId() : null);
    dto.setCode(entity.getCode());
    dto.setName(entity.getName());
    dto.setDescription(entity.getDescription());
    dto.setSortingOrder(entity.getSortingOrder());

    return dto;
  }

  @Nonnull
  @Override
  public CourseLevelEntity toNewEntity(@Nonnull final CourseLevelDto dto) {
    final var entity = CourseLevelEntity.empty();
    updateEntity(entity, dto);
    return entity;
  }

  @Override
  public void updateEntity(@Nonnull final CourseLevelEntity entity,
                           @Nonnull final CourseLevelDto dto) {
    entity.setCode(dto.getCode());
    entity.setName(dto.getName());
    entity.setDescription(dto.getDescription());
    entity.setSortingOrder(dto.getSortingOrder());
  }
}