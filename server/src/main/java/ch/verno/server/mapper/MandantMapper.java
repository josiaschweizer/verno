package ch.verno.server.mapper;

import ch.verno.common.db.dto.table.MandantDto;
import ch.verno.db.entity.mandant.MandantEntity;
import jakarta.annotation.Nonnull;

public final class MandantMapper {

  private MandantMapper() {
  }

  @Nonnull
  public static MandantDto toDto(@Nonnull final MandantEntity entity) {
    return new MandantDto(
            entity.getId(),
            entity.getSlug(),
            entity.getName()
    );
  }

  @Nonnull
  public static MandantEntity toEntity(@Nonnull final MandantDto dto) {
    final MandantEntity entity = MandantEntity.ref(dto.getId());
    entity.setSlug(dto.getSlug());
    entity.setName(dto.getName());
    return entity;
  }
}