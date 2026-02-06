package ch.verno.server.mapper;

import ch.verno.common.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.publ.Publ;
import ch.verno.db.entity.ParticipantEntity;
import ch.verno.db.entity.mandant.MandantEntity;
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

    final var dto = new ParticipantDto(
            entity.getId(),
            entity.getFirstname() == null ? Publ.EMPTY_STRING : entity.getFirstname(),
            entity.getLastname() == null ? Publ.EMPTY_STRING : entity.getLastname(),
            birthdate,
            GenderMapper.toDto(entity.getGender()),
            entity.getEmail() == null ? Publ.EMPTY_STRING : entity.getEmail(),
            entity.getPhone() == null ? PhoneNumber.empty() : PhoneNumber.fromString(entity.getPhone()),
            entity.getNote() == null ? Publ.EMPTY_STRING : entity.getNote(),
            entity.isActive(),
            entity.getCourses().stream().map(CourseMapper::toDto).toList(),
            entity.getCourseLevels().stream().map(CourseLevelMapper::toDto).toList(),
            AddressMapper.toDto(entity.getAddress()),
            ParentMapper.toDto(entity.getParentOne()),
            ParentMapper.toDto(entity.getParentTwo())
    );

    if (entity.getMandant() != null) {
      dto.setMandantId(entity.getMandant().getId());
    }

    return dto;
  }

  @Nullable
  public static ParticipantEntity toEntity(@Nullable final ParticipantDto dto, final long mandantId) {
    if (dto == null) {
      return null;
    }

    final var entity = new ParticipantEntity(
            MandantEntity.ref(mandantId),
            dto.getFirstName(),
            dto.getLastName(),
            dto.getBirthdate() != null ? dto.getBirthdate() : LocalDate.now(),
            dto.getEmail(),
            !dto.getPhone().isEmpty() ? dto.getPhone().toString() : Publ.EMPTY_STRING,
            dto.getNote(),
            dto.isActive()
    );

    if (dto.getId() != null && dto.getId() != 0) {
      entity.setId(dto.getId());
    } else {
      entity.setId(null);
    }

    entity.setGender(GenderMapper.toEntityRef(dto.getGender()));
    entity.setCourseLevels(dto.getCourseLevels().stream().map(CourseLevelMapper::toEntityRef).toList());
    entity.setCourses(dto.getCourses().stream().map(CourseMapper::toEntityRef).toList());

    entity.setAddress(AddressMapper.toEntity(dto.getAddress(), mandantId));
    entity.setParentOne(ParentMapper.toEntity(dto.getParentOne(), mandantId));
    entity.setParentTwo(ParentMapper.toEntity(dto.getParentTwo(), mandantId));

    return entity;
  }
}