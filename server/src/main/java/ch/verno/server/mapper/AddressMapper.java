package ch.verno.server.mapper;

import ch.verno.common.db.dto.table.AddressDto;
import ch.verno.db.entity.AddressEntity;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public final class AddressMapper {
  private AddressMapper() {
  }

  @Nonnull
  public static AddressDto toDto(@Nullable final AddressEntity entity) {
    if (entity == null) {
      return AddressDto.empty();
    }

    final var dto = new AddressDto(
        entity.getId(),
        entity.getStreet() == null ? Publ.EMPTY_STRING : entity.getStreet(),
        entity.getHouseNumber() == null ? Publ.EMPTY_STRING : entity.getHouseNumber(),
        entity.getZipCode() == null ? Publ.EMPTY_STRING : entity.getZipCode(),
        entity.getCity() == null ? Publ.EMPTY_STRING : entity.getCity(),
        entity.getCountry() == null ? Publ.EMPTY_STRING : entity.getCountry()
    );

    if (entity.getTenant() != null) {
      dto.setTenantId(entity.getTenant().getId());
    }

    return dto;
  }

  @Nullable
  public static AddressEntity toEntity(@Nullable final AddressDto dto, final long tenantId) {
    if (dto == null || dto.isEmpty()) {
      return null;
    }

    final var entity = new AddressEntity(
            TenantEntity.ref(tenantId),
        dto.getStreet(),
        dto.getHouseNumber(),
        dto.getZipCode(),
        dto.getCity(),
        dto.getCountry()
    );

    if (dto.getId() != null && dto.getId() != 0) {
      entity.setId(dto.getId());
    } else {
      entity.setId(null);
    }

    return entity;
  }
}