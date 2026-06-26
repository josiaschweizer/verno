package ch.verno.server.mapper.address;

import ch.verno.contract.dto.table.address.AddressDto;
import ch.verno.db.entity.address.AddressEntity;
import ch.verno.server.mapper.base.IEntityMapper;
import ch.verno.server.util.ServerStringUtil;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper implements IEntityMapper<AddressEntity, AddressDto> {

  @Nonnull
  @Override
  public AddressDto toSimpleDto(@Nonnull final AddressEntity entity) {
    final var dto = AddressDto.empty();

    dto.setId(entity.getId());
    dto.setStreet(entity.getStreet());
    dto.setHouseNumber(entity.getHouseNumber());
    dto.setZipCode(entity.getZipCode());
    dto.setCity(entity.getCity());
    dto.setCountry(entity.getCountry());

    return dto;
  }

  @Nonnull
  @Override
  public AddressEntity toNewEntity(@Nonnull final AddressDto dto) {
    return new AddressEntity(
            ServerStringUtil.safeString(dto.getStreet()),
            ServerStringUtil.safeString(dto.getHouseNumber()),
            ServerStringUtil.safeString(dto.getZipCode()),
            ServerStringUtil.safeString(dto.getCity()),
            ServerStringUtil.safeString(dto.getCountry())
    );
  }

  @Override
  public void updateEntity(@Nonnull final AddressEntity entity,
                           @Nonnull final AddressDto dto) {
    entity.setStreet(ServerStringUtil.safeString(dto.getStreet()));
    entity.setHouseNumber(ServerStringUtil.safeString(dto.getHouseNumber()));
    entity.setZipCode(ServerStringUtil.safeString(dto.getZipCode()));
    entity.setCity(ServerStringUtil.safeString(dto.getCity()));
    entity.setCountry(ServerStringUtil.safeString(dto.getCountry()));
  }
}