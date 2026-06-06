package ch.verno.server.mapper.text;

import ch.verno.common.db.dto.table.text.TextDto;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.db.entity.text.TextEntity;
import jakarta.annotation.Nonnull;

public class TextMapper {

  @Nonnull
  public static TextDto toDto(@Nonnull final TextEntity entity) {
    final var dto = new TextDto(
            entity.getIdentifier(),
            entity.getSubIdentifier(),
            entity.getLanguage(),
            entity.getText()
    );
    dto.setId(entity.getId());

    return dto;
  }

  @Nonnull
  public static TextEntity toNewEntity(@Nonnull final TextDto dto,
                                       @Nonnull final TenantEntity tenant) {
    final var entity = new TextEntity(
            dto.getIdentifier(),
            dto.getSubIdentifier(),
            dto.getLanguage().getCode(),
            dto.getText()
    );
    entity.setTenant(tenant);

    return entity;
  }

  public static void updateEntity(@Nonnull final TextEntity entity,
                                  @Nonnull final TextDto dto) {
    entity.setIdentifier(dto.getIdentifier());
    entity.setSubIdentifier(dto.getSubIdentifier());
    entity.setLanguageCode(dto.getLanguage().getCode());
    entity.setText(dto.getText());
  }

}