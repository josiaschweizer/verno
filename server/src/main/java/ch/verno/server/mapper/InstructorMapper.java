package ch.verno.server.mapper;

import ch.verno.common.ui.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.db.dto.table.InstructorDto;
import ch.verno.db.entity.InstructorEntity;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public final class InstructorMapper {
  private InstructorMapper() {
  }

  @Nonnull
  public static InstructorDto toDto(@Nullable final InstructorEntity entity) {
    if (entity == null) {
      return new InstructorDto();
    }

    final var dto = new InstructorDto(
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
  public static InstructorEntity toEntity(@Nullable final InstructorDto dto, final long tenantId) {
    if (dto == null || dto.isEmpty()) {
      return null;
    }

    final var entity = new InstructorEntity(
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

  @Nonnull
  public static InstructorEntity updateEntity(@Nonnull final InstructorEntity entity,
                                              @Nonnull final InstructorDto dto,
                                              final long tenantId) {
    entity.setFirstname(dto.getFirstName());
    entity.setLastname(dto.getLastName());
    entity.setEmail(dto.getEmail());
    entity.setPhone(dto.getPhone().toString());
    entity.setGender(GenderMapper.toEntity(dto.getGender()));
    entity.setAddress(AddressMapper.toEntity(dto.getAddress(), tenantId));

    // do not overwrite Tenant on update here (preserve existing)
    return entity;
  }
}