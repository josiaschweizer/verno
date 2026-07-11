package ch.verno.server.mapper.tenant;

import ch.verno.contract.dto.table.tenant.TenantDto;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.server.mapper.base.AbstractEntityMapper;
import ch.verno.server.mapper.base.MapperContext;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class TenantMapper extends AbstractEntityMapper<TenantEntity, TenantDto> {

  @Nonnull
  @Override
  public TenantDto toDto(@Nonnull final TenantEntity entity) {
    return new TenantDto(
            entity.getId(),
            entity.getSlug(),
            entity.getName()
    );
  }

  @Nonnull
  @Override
  public TenantEntity toNewEntity(@Nonnull final TenantDto dto) {
    // set tenant id to new tenant id (exceptionally for the tenant entity)
    final var entity = TenantEntity.ref(dto.id());
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