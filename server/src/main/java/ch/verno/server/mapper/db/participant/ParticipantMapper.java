package ch.verno.server.mapper.db.participant;

import ch.verno.common.dto.ui.phonenumber.PhoneNumber;
import ch.verno.contract.dto.table.address.AddressDto;
import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.course.CourseLevelDto;
import ch.verno.contract.dto.table.gender.GenderDto;
import ch.verno.contract.dto.table.participant.ParentDto;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.db.entity.address.AddressEntity;
import ch.verno.db.entity.course.CourseEntity;
import ch.verno.db.entity.course.CourseLevelEntity;
import ch.verno.db.entity.gender.GenderEntity;
import ch.verno.db.entity.participant.ParentEntity;
import ch.verno.db.entity.participant.ParticipantEntity;
import ch.verno.server.mapper.db.base.IEntityMapper;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class ParticipantMapper implements IEntityMapper<ParticipantEntity, ParticipantDto> {

  @Nonnull
  @Override
  public ParticipantDto toSimpleDto(@Nonnull final ParticipantEntity entity) {
    final var dto = ParticipantDto.empty();

    dto.setId(entity.getId());
    dto.setTenantId(entity.getTenant() != null ? entity.getTenant().getId() : null);
    dto.setFirstName(entity.getFirstname());
    dto.setLastName(entity.getLastname());
    dto.setBirthdate(entity.getBirthdate());
    dto.setGender(entity.getGender() == null ? GenderDto.empty() : GenderDto.ref(entity.getGender().getId()));
    dto.setEmail(entity.getEmail());
    dto.setPhone(PhoneNumber.ofNullable(entity.getPhone()));
    dto.setNote(entity.getNote());
    dto.setActive(entity.isActive());

    dto.setCourseLevels(
            entity.getCourseLevels()
                    .stream()
                    .map(CourseLevelEntity::getId)
                    .map(CourseLevelDto::ref)
                    .toList()
    );

    dto.setCourses(
            entity.getCourses()
                    .stream()
                    .map(CourseEntity::getId)
                    .map(CourseDto::ref)
                    .toList()
    );

    dto.setAddress(entity.getAddress() == null ? AddressDto.empty() : AddressDto.ref(entity.getAddress().getId()));
    dto.setParentOne(entity.getParentOne() == null ? ParentDto.empty() : ParentDto.ref(entity.getParentOne().getId()));
    dto.setParentTwo(entity.getParentTwo() == null ? ParentDto.empty() : ParentDto.ref(entity.getParentTwo().getId()));

    dto.setSiblings(
            entity.getSiblings()
                    .stream()
                    .map(ParticipantEntity::getId)
                    .map(ParticipantDto::ref)
                    .toList()
    );

    return dto;
  }

  @Nonnull
  @Override
  public ParticipantEntity toNewEntity(@Nonnull final ParticipantDto dto) {
    final var entity = ParticipantEntity.empty();
    updateEntity(entity, dto);
    return entity;
  }

  @Override
  public void updateEntity(@Nonnull final ParticipantEntity entity,
                           @Nonnull final ParticipantDto dto) {
    entity.setFirstname(dto.getFirstName());
    entity.setLastname(dto.getLastName());
    entity.setBirthdate(dto.getBirthdate());
    entity.setEmail(dto.getEmail());
    entity.setPhone(dto.getPhone().toString());
    entity.setNote(dto.getNote());
    entity.setActive(dto.isActive());

    entity.setGender(dto.getGender().getId() == null ? null : GenderEntity.ref(dto.getGender().getId()));
    entity.setAddress(dto.getAddress().getId() == null ? null : AddressEntity.ref(dto.getAddress().getId()));
    entity.setParentOne(dto.getParentOne().getId() == null ? null : ParentEntity.ref(dto.getParentOne().getId()));
    entity.setParentTwo(dto.getParentTwo().getId() == null ? null : ParentEntity.ref(dto.getParentTwo().getId()));

    entity.setCourseLevels(dto.getCourseLevels()
            .stream()
            .filter(level -> level.getId() != null)
            .map(level -> CourseLevelEntity.ref(level.getId()))
            .toList()
    );

    entity.setCourses(dto.getCourses()
            .stream()
            .filter(course -> course.getId() != null)
            .map(course -> CourseEntity.ref(course.getId()))
            .toList()
    );

    entity.setSiblings(dto.getSiblings()
            .stream()
            .filter(sibling -> sibling.getId() != null)
            .map(sibling -> ParticipantEntity.ref(sibling.getId()))
            .toList()
    );
  }
}
