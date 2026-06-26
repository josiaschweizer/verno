package ch.verno.server.mapper.user;

import ch.verno.common.db.role.Role;
import ch.verno.contract.dto.table.user.AppUserDto;
import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.server.mapper.base.IEntityMapper;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class AppUserMapper implements IEntityMapper<AppUserEntity, AppUserDto> {

  @Nonnull
  @Override
  public AppUserDto toSimpleDto(@Nonnull final AppUserEntity entity) {
    final var dto = AppUserDto.empty();

    dto.setId(entity.getId());
    dto.setUsername(entity.getUsername());
    dto.setFirstname(entity.getFirstname());
    dto.setLastname(entity.getLastname());
    dto.setEmail(entity.getEmail());
    dto.setPasswordHash(entity.getPasswordHash());
    dto.setRole(Role.valueOf(entity.getRole()));
    dto.setActive(entity.isActive());

    return dto;
  }

  @Nonnull
  @Override
  public AppUserEntity toNewEntity(@Nonnull final AppUserDto dto) {
    final var entity = AppUserEntity.empty();

    updateEntity(entity, dto);

    return entity;
  }

  @Override
  public void updateEntity(@Nonnull final AppUserEntity entity,
                           @Nonnull final AppUserDto dto) {
    entity.setUsername(dto.getUsername());
    entity.setFirstname(dto.getFirstname());
    entity.setLastname(dto.getLastname());
    entity.setEmail(dto.getEmail());
    entity.setPasswordHash(dto.getPasswordHash());
    entity.setRole(dto.getRole().name());
    entity.setActive(dto.isActive());
  }
}