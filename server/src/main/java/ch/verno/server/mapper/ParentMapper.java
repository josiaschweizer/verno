package ch.verno.server.mapper;

import ch.verno.common.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.db.dto.ParentDto;
import ch.verno.common.util.Publ;
import ch.verno.db.entity.ParentEntity;
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

    return new ParentDto(
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
  public static ParentEntity toEntity(@Nullable final ParentDto dto) {
    if (dto == null || dto.isEmpty()) {
      return null;
    }

    final ParentEntity entity = new ParentEntity(
        dto.firstName(),
        dto.lastName(),
        dto.email(),
        dto.phoneNumber().isEmpty()
            ? Publ.EMPTY_STRING
            : dto.phoneNumber().toString()
    );

    if (dto.id() != null) {
      entity.setId(dto.id());
    }

    entity.setGender(GenderMapper.toEntity(dto.gender()));
    entity.setAddress(AddressMapper.toEntity(dto.address()));

    return entity;
  }
}