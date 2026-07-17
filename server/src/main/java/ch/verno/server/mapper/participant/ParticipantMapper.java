package ch.verno.server.mapper.participant;

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
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.address.AddressMapper;
import ch.verno.server.mapper.base.AbstractEntityMapper;
import ch.verno.server.mapper.course.CourseLevelMapper;
import ch.verno.server.mapper.course.CourseMapper;
import ch.verno.server.mapper.gender.GenderMapper;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class ParticipantMapper extends AbstractEntityMapper<ParticipantEntity, ParticipantDto> {

  public ParticipantMapper(@Nonnull final ServerBean serverBean) {
    setContextMappers(
            this,
            serverBean.get(GenderMapper.class),
            serverBean.get(ParentMapper.class),
            serverBean.get(CourseMapper.class),
            serverBean.get(AddressMapper.class),
            serverBean.get(CourseLevelMapper.class)
    );
  }

  @Nonnull
  @Override
  public ParticipantDto toDto(@Nonnull final ParticipantEntity entity) {
    final var dto = ParticipantDto.empty();

    dto.setId(entity.getId());
    dto.setTenantId(entity.getTenant() == null ? null : entity.getTenant().getId());
    dto.setFirstName(entity.getFirstname());
    dto.setLastName(entity.getLastname());
    dto.setBirthdate(entity.getBirthdate());
    dto.setGender(mapReference(entity.getGender(), GenderMapper.class, GenderDto::empty, e -> GenderDto.ref(e.getId())));
    dto.setEmail(entity.getEmail());
    dto.setPhone(PhoneNumber.ofNullable(entity.getPhone()));
    dto.setNote(entity.getNote());
    dto.setActive(entity.isActive());

    dto.setAddress(mapReference(entity.getAddress(), AddressMapper.class, AddressDto::empty, e -> AddressDto.ref(e.getId())));
    dto.setParentOne(mapReference(entity.getParentOne(), ParentMapper.class, ParentDto::empty, e -> ParentDto.ref(e.getId())));
    dto.setParentTwo(mapReference(entity.getParentTwo(), ParentMapper.class, ParentDto::empty, e -> ParentDto.ref(e.getId())));

    dto.setCourseLevels(mapReferences(entity.getCourseLevels(), CourseLevelMapper.class, CourseLevelDto::empty, e -> CourseLevelDto.ref(e.getId())));
    dto.setCourses(mapReferences(entity.getCourses(), CourseMapper.class, CourseDto::empty, e -> CourseDto.ref(e.getId())));

    dto.setSiblings(mapReferences(entity.getSiblings(), ParticipantMapper.class, ParticipantDto::empty, e -> ParticipantDto.ref(e.getId())));

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