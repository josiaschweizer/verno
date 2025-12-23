package ch.verno.server.mapper;

import ch.verno.common.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.db.dto.InstructorDto;
import ch.verno.common.util.Publ;
import ch.verno.db.entity.InstructorEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public final class InstructorMapper {
  private InstructorMapper() {
  }

  @Nonnull
  public static InstructorDto toDto(@Nullable final InstructorEntity entity) {
    if (entity == null) {
      return InstructorDto.empty();
    }

    return new InstructorDto(
        entity.getId(),
        entity.getFirstname() == null ? Publ.EMPTY_STRING : entity.getFirstname(),
        entity.getLastname() == null ? Publ.EMPTY_STRING : entity.getLastname(),
        entity.getEmail() == null ? Publ.EMPTY_STRING : entity.getEmail(),
        entity.getPhone() == null ? PhoneNumber.empty() : PhoneNumber.fromString(entity.getPhone()),
        GenderMapper.toDto(entity.getGender()),
        AddressMapper.toDto(entity.getAddress())
    );
  }

  @Nullable
  public static InstructorEntity toEntity(@Nullable final InstructorDto dto) {
    if (dto == null || dto.isEmpty()) {
      return null;
    }

    final InstructorEntity entity = new InstructorEntity(
        dto.firstName(),
        dto.lastName(),
        dto.email(),
        dto.phone().isEmpty() ? Publ.EMPTY_STRING : dto.phone().toString()
    );

    if (dto.id() != null) {
      entity.setId(dto.id());
    }

    entity.setGender(GenderMapper.toEntity(dto.gender()));
    entity.setAddress(AddressMapper.toEntity(dto.address()));

    return entity;
  }
}