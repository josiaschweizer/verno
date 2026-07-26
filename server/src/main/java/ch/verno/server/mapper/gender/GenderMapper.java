package ch.verno.server.mapper.gender;

import ch.verno.contract.dto.table.gender.GenderDto;
import ch.verno.db.entity.gender.GenderEntity;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.base.AbstractEntityMapper;
import ch.verno.server.mapper.text.TextMapper;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class GenderMapper extends AbstractEntityMapper<GenderEntity, GenderDto> {

  public GenderMapper(@Nonnull final ServerBean serverBean) {
    setContextMappers(serverBean.get(TextMapper.class));
  }

  @Nonnull
  @Override
  public GenderDto toDto(@Nonnull final GenderEntity entity) {
    final var dto = GenderDto.empty();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setDescription(entity.getDescription());

    //TODO why the fuck do we need the gender translation context???
    getMapperContext().find(GenderTranslationContext.class).ifPresent(ctx -> dto.setUserDisplayTexts(ctx.translations()));

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