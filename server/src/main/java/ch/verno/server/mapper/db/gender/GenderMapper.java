package ch.verno.server.mapper.db.gender;

import ch.verno.contract.dto.table.gender.GenderDto;
import ch.verno.db.entity.gender.GenderEntity;
import ch.verno.server.mapper.db.base.IEntityMapper;
import ch.verno.server.mapper.db.base.MapperContext;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class GenderMapper implements IEntityMapper<GenderEntity, GenderDto> {

  @Nonnull
  @Override
  public GenderDto toSimpleDto(@Nonnull final GenderEntity entity) {
    final var dto = GenderDto.empty();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setDescription(entity.getDescription());

    return dto;
  }

  @Nonnull
  @Override
  public GenderDto toDto(@Nonnull final GenderEntity entity,
                         @Nonnull final MapperContext context) {
    final var dto = toSimpleDto(entity);
    context.find(GenderTranslationContext.class).ifPresent(ctx -> dto.setUserDisplayTexts(ctx.translations()));
    return dto;
  }

  @Nonnull
  @Override
  public GenderEntity toNewEntity(@Nonnull final GenderDto dto) {
    final var entity = GenderEntity.empty();
    updateEntity(entity, dto);
    return entity;
  }

  @Override
  public void updateEntity(@Nonnull final GenderEntity entity,
                           @Nonnull final GenderDto dto) {
    entity.setName(dto.getName());
    entity.setDescription(dto.getDescription());
  }
}