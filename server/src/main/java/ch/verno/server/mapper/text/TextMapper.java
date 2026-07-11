package ch.verno.server.mapper.text;

import ch.verno.contract.dto.table.text.TextDto;
import ch.verno.db.entity.text.TextEntity;
import ch.verno.server.mapper.base.AbstractEntityMapper;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class TextMapper extends AbstractEntityMapper<TextEntity, TextDto> {

  @Nonnull
  @Override
  public TextDto toDto(@Nonnull final TextEntity entity) {
    final var dto = TextDto.empty();

    dto.setId(entity.getId());
    dto.setTenantId(entity.getTenant().getId());

    dto.setIdentifier(entity.getIdentifier());
    dto.setSubIdentifier(entity.getSubIdentifier());

    dto.setLanguage(entity.getLanguage());
    dto.setText(entity.getText());

    return dto;
  }

  @Nonnull
  @Override
  public TextEntity toNewEntity(@Nonnull final TextDto dto) {
    final var entity = TextEntity.empty();

    updateEntity(entity, dto);

    return entity;
  }

  @Override
  public void updateEntity(@Nonnull final TextEntity entity,
                           @Nonnull final TextDto dto) {
    entity.setIdentifier(dto.getIdentifier());
    entity.setSubIdentifier(dto.getSubIdentifier());

    entity.setLanguageCode(dto.getLanguage().getCode());
    entity.setText(dto.getText());
  }

}