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
      return ParticipantDto.empty();
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
        CourseLevelMapper.toDto(entity.getCourseLevel()),
        CourseMapper.toDto(entity.getCourse()),
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
        dto.firstName(),
        dto.lastName(),
        dto.birthdate(),
        dto.email(),
        dto.phone().isEmpty()
            ? null
            : dto.phone()
    );

    // ID setzen → entscheidet über INSERT vs UPDATE
    if (dto.id() != null) {
      entity.setId(dto.id());
    }

    entity.setGender(GenderMapper.toEntity(dto.gender()));
    entity.setCourseLevel(CourseLevelMapper.toEntity(dto.courseLevel()));
    entity.setCourse(CourseMapper.toEntity(dto.course()));
    entity.setAddress(AddressMapper.toEntity(dto.address()));
    entity.setParentOne(ParentMapper.toEntity(dto.parentOne()));
    entity.setParentTwo(ParentMapper.toEntity(dto.parentTwo()));

    return entity;
  }

}