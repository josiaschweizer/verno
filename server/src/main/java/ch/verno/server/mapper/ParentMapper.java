package ch.verno.server.mapper;

import ch.verno.common.ui.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.db.dto.table.ParentDto;
import ch.verno.db.entity.ParentEntity;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public final class ParentMapper {
  private ParentMapper() {
  }

  @Nonnull
  public static ParentDto toDto(@Nullable final ParentEntity entity) {
    if (entity == null) {
      return ParentDto.empty();
    }

    final var dto = new ParentDto(
            entity.getId(),
            entity.getFirstname() == null ? Publ.EMPTY_STRING : entity.getFirstname(),
            entity.getLastname() == null ? Publ.EMPTY_STRING : entity.getLastname(),
            entity.getEmail() == null ? Publ.EMPTY_STRING : entity.getEmail(),
            entity.getPhone() == null ? PhoneNumber.empty() : PhoneNumber.fromString(entity.getPhone()),
            GenderMapper.toDto(entity.getGender()),
            AddressMapper.toDto(entity.getAddress())
    );

    if (entity.getTenant() != null) {
      dto.setTenantId(entity.getTenant().getId());
    }

    return dto;
  }

  @Nullable
  public static ParentEntity toEntity(@Nullable final ParentDto dto, final long tenantId) {
    if (dto == null || dto.isEmpty()) {
      return null;
    }

    final var entity = new ParentEntity(
            TenantEntity.ref(tenantId),
            dto.getFirstName(),
            dto.getLastName(),
            dto.getEmail(),
            dto.getPhone().isEmpty() ? Publ.EMPTY_STRING : dto.getPhone().toString()
    );

    if (dto.getId() != null && dto.getId() != 0) {
      entity.setId(dto.getId());
    } else {
      entity.setId(null);
    }

    entity.setGender(GenderMapper.toEntityRef(dto.getGender()));
    entity.setAddress(AddressMapper.toEntity(dto.getAddress(), tenantId));

    return entity;
  }
}