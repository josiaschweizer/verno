package ch.verno.server.mapper.instructor;

import ch.verno.common.dto.ui.phonenumber.PhoneNumber;
import ch.verno.contract.dto.table.address.AddressDto;
import ch.verno.contract.dto.table.gender.GenderDto;
import ch.verno.contract.dto.table.instructor.InstructorDto;
import ch.verno.db.entity.address.AddressEntity;
import ch.verno.db.entity.gender.GenderEntity;
import ch.verno.db.entity.instructor.InstructorEntity;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.address.AddressMapper;
import ch.verno.server.mapper.base.AbstractEntityMapper;
import ch.verno.server.mapper.gender.GenderMapper;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

@Component
public class InstructorMapper extends AbstractEntityMapper<InstructorEntity, InstructorDto> {

  public InstructorMapper(@Nonnull final ServerBean serverBean) {
    setContextMappers(
            serverBean.get(GenderMapper.class),
            serverBean.get(AddressMapper.class)
    );
  }

  @Nonnull
  @Override
  public InstructorDto toDto(@Nonnull final InstructorEntity entity) {
    final var dto = InstructorDto.empty();

    dto.setId(entity.getId());
    dto.setTenantId(entity.getTenant() != null ? entity.getTenant().getId() : null);
    dto.setFirstName(entity.getFirstname());
    dto.setLastName(entity.getLastname());
    dto.setEmail(entity.getEmail());
    dto.setPhone(PhoneNumber.ofNullable(entity.getPhone()));
    dto.setGender(mapReference(entity.getGender(), GenderMapper.class, GenderDto::empty, e -> GenderDto.ref(e.getId())));
    dto.setAddress(mapReference(entity.getAddress(), AddressMapper.class, AddressDto::empty, e -> AddressDto.ref(e.getId())));

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