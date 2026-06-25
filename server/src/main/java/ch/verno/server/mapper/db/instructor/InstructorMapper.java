package ch.verno.server.mapper.db.instructor;

import ch.verno.common.dto.ui.phonenumber.PhoneNumber;
import ch.verno.contract.dto.table.address.AddressDto;
import ch.verno.contract.dto.table.gender.GenderDto;
import ch.verno.contract.dto.table.instructor.InstructorDto;
import ch.verno.db.entity.address.AddressEntity;
import ch.verno.db.entity.gender.GenderEntity;
import ch.verno.db.entity.instructor.InstructorEntity;
import ch.verno.server.mapper.db.base.IEntityMapper;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class InstructorMapper implements IEntityMapper<InstructorEntity, InstructorDto> {

  @Nonnull
  @Override
  public InstructorDto toSimpleDto(@Nonnull final InstructorEntity entity) {
    final var dto = InstructorDto.empty();

    dto.setId(entity.getId());
    dto.setTenantId(entity.getTenant() != null ? entity.getTenant().getId() : null);
    dto.setFirstName(entity.getFirstname());
    dto.setLastName(entity.getLastname());
    dto.setEmail(entity.getEmail());
    dto.setPhone(PhoneNumber.ofNullable(entity.getPhone()));
    dto.setGender(entity.getGender() == null ? GenderDto.empty() : GenderDto.ref(entity.getGender().getId()));
    dto.setAddress(entity.getAddress() == null ? AddressDto.empty() : AddressDto.ref(entity.getAddress().getId()));

    return dto;
  }

  @Nonnull
  @Override
  public InstructorEntity toNewEntity(@Nonnull final InstructorDto dto) {
    final var entity = InstructorEntity.empty();
    updateEntity(entity, dto);
    return entity;
  }

  @Override
  public void updateEntity(@Nonnull final InstructorEntity entity,
                           @Nonnull final InstructorDto dto) {
    entity.setFirstname(dto.getFirstName());
    entity.setLastname(dto.getLastName());
    entity.setEmail(dto.getEmail());
    entity.setPhone(dto.getPhone().toString());
    entity.setGender(dto.getGender().getId() == null ? null : GenderEntity.ref(dto.getGender().getId()));
    entity.setAddress(dto.getAddress().getId() == null ? null : AddressEntity.ref(dto.getAddress().getId()));
  }
}