package ch.verno.server.mapper;

import ch.verno.common.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.common.util.Publ;
import ch.verno.db.entity.ParticipantEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.LocalDate;

public final class ParticipantMapper {
  private ParticipantMapper() {
  }

  @Nonnull
  public static ParticipantDto toDto(@Nullable final ParticipantEntity entity) {
    if (entity == null) {
      return new ParticipantDto();
    }

    final var birthdate = entity.getBirthdate() == null ? LocalDate.now() : entity.getBirthdate();

    return new ParticipantDto(
        entity.getId(),
        entity.getFirstname() == null ? Publ.EMPTY_STRING : entity.getFirstname(),
        entity.getLastname() == null ? Publ.EMPTY_STRING : entity.getLastname(),
        birthdate,
        GenderMapper.toDto(entity.getGender()),
        entity.getEmail() == null ? Publ.EMPTY_STRING : entity.getEmail(),
        entity.getPhone() == null ? PhoneNumber.empty() : PhoneNumber.fromString(entity.getPhone()),
        CourseMapper.toDto(entity.getCourse()),
        CourseLevelMapper.toDto(entity.getCourseLevel()),
        AddressMapper.toDto(entity.getAddress()),
        ParentMapper.toDto(entity.getParentOne()),
        ParentMapper.toDto(entity.getParentTwo())
    );
  }

  public static ParticipantEntity toEntity(@Nullable final ParticipantDto dto) {
    if (dto == null) {
      return null;
    }

    final var entity = new ParticipantEntity(
        dto.getFirstName(),
        dto.getLastName(),
        dto.getBirthdate() != null ?
            dto.getBirthdate() :
            LocalDate.now(),
        dto.getEmail(),
        dto.getPhone().isEmpty() ?
            Publ.EMPTY_STRING :
            dto.getPhone().toString()
    );

    if (dto.getId() != null && dto.getId() != 0) {
      entity.setId(dto.getId());
    } else {
      entity.setId(null);
    }

    entity.setGender(GenderMapper.toEntity(dto.getGender()));
    entity.setCourseLevel(CourseLevelMapper.toEntity(dto.getCourseLevel()));
    entity.setCourse(CourseMapper.toEntity(dto.getCourse()));
    entity.setAddress(AddressMapper.toEntity(dto.getAddress()));
    entity.setParentOne(ParentMapper.toEntity(dto.getParentOne()));
    entity.setParentTwo(ParentMapper.toEntity(dto.getParentTwo()));

    return entity;
  }

}