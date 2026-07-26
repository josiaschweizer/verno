package ch.verno.server.mapper.participant;

import ch.verno.common.dto.ui.phonenumber.PhoneNumber;
import ch.verno.contract.dto.table.address.AddressDto;
import ch.verno.contract.dto.table.gender.GenderDto;
import ch.verno.contract.dto.table.participant.ParentDto;
import ch.verno.db.entity.address.AddressEntity;
import ch.verno.db.entity.gender.GenderEntity;
import ch.verno.db.entity.participant.ParentEntity;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.address.AddressMapper;
import ch.verno.server.mapper.base.AbstractEntityMapper;
import ch.verno.server.mapper.gender.GenderMapper;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class ParentMapper extends AbstractEntityMapper<ParentEntity, ParentDto> {

  public ParentMapper(@Nonnull final ServerBean serverBean) {
    setContextMappers(
            serverBean.get(GenderMapper.class),
            serverBean.get(AddressMapper.class)
    );
  }

  @Nonnull
  @Override
  public ParentDto toDto(@Nonnull final ParentEntity entity) {
    final var dto = ParentDto.empty();

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
  public ParentEntity toNewEntity(@Nonnull final ParentDto dto) {
    final var entity = ParentEntity.empty();
    updateEntity(entity, dto);
    return entity;
  }

  @Override
  public void updateEntity(@Nonnull final ParentEntity entity,
                           @Nonnull final ParentDto dto) {
    entity.setFirstname(dto.getFirstName());
    entity.setLastname(dto.getLastName());
    entity.setEmail(dto.getEmail());
    entity.setPhone(dto.getPhone().toString());
    entity.setGender(dto.getGender().getId() == null ? null : GenderEntity.ref(dto.getGender().getId()));
    entity.setAddress(dto.getAddress().getId() == null ? null : AddressEntity.ref(dto.getAddress().getId()));
  }
}