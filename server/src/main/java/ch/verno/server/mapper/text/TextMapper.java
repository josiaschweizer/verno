package ch.verno.server.mapper.text;

import ch.verno.common.db.dto.table.text.TextDto;
import ch.verno.db.entity.text.TextEntity;
import jakarta.annotation.Nonnull;

public class TextMapper {

  @Nonnull
  public static TextDto toDto(@Nonnull TextEntity entity) {
    return new TextDto(
            entity.getIdentifier(),
            entity.getSubIdentifier(),
            entity.getLanguage(),
            entity.getText()
    );
  }

  @Nonnull
  public static TextEntity toEntity(@Nonnull TextDto dto) {
    return new TextEntity(
            dto.getIdentifier(),
            dto.getSubIdentifier(),
            dto.getLanguage().getCode(),
            dto.getText()
    );
  }

}
