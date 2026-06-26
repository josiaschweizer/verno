package ch.verno.server.mapper.tenant;

import ch.verno.contract.dto.table.tenant.TenantDto;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.server.mapper.base.IEntityMapper;
import ch.verno.server.mapper.base.MapperContext;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class TenantMapper implements IEntityMapper<TenantEntity, TenantDto> {

  @Nonnull
  @Override
  public TenantDto toSimpleDto(@Nonnull final TenantEntity entity) {
    return new TenantDto(
            entity.getId(),
            entity.getSlug(),
            entity.getName()
    );
  }

  @Nonnull
  @Override
  public TenantDto toDto(@Nonnull final TenantEntity entity,
                         @Nonnull final MapperContext context) {
    return toSimpleDto(entity);
  }

  @Nonnull
  @Override
  public TenantEntity toNewEntity(@Nonnull final TenantDto dto) {
    final var entity = TenantEntity.empty();
    updateEntity(entity, dto);
    return entity;
  }

  @Override
  public void updateEntity(@Nonnull final TenantEntity entity,
                           @Nonnull final TenantDto dto) {
    entity.setSlug(dto.slug());
    entity.setName(dto.name());
  }
}