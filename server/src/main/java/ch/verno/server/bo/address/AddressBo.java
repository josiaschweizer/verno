package ch.verno.server.bo.address;

import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.contract.dto.table.address.AddressDto;
import ch.verno.db.entity.address.AddressEntity;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.address.AddressMapper;
import ch.verno.server.repository.address.AddressRepository;
import ch.verno.server.util.ServerStringUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

@Component
public class AddressBo {

  @Nonnull private final Lazy<AddressMapper> addressMapper;
  @Nonnull private final Lazy<AddressRepository> addressRepository;

  protected AddressBo(@Nonnull final ServerBean bean) {
    this.addressMapper = Lazy.of(() -> bean.get(AddressMapper.class));
    this.addressRepository = Lazy.of(() -> bean.get(AddressRepository.class));
  }

  @Nullable
  public AddressEntity saveOrUpdate(@Nullable final AddressDto dto) {
    if (dto == null || !hasContent(dto)) {
      return null;
    }

    final var sanitizedDto = sanitize(dto);
    if (sanitizedDto.getId() != null && sanitizedDto.getId() != 0L) {
      final var entity = addressRepository.get().findById(sanitizedDto.getId())
              .orElseThrow(() -> new DBNotFoundException(
                      DBNotFoundReason.ADDRESS_BY_ID_NOT_FOUND,
                      sanitizedDto.getId()
              ));

      addressMapper.get().updateEntity(entity, sanitizedDto);
      return addressRepository.get().save(entity);
    }
    return addressRepository.get().save(addressMapper.get().toNewEntity(sanitizedDto));
  }

  @Nonnull
  public AddressEntity findOrCreate(@Nonnull final AddressDto dto) {
    final var sanitizedDto = sanitize(dto);

    return addressRepository.get()
            .findByFields(
                    sanitizedDto.getStreet(),
                    sanitizedDto.getHouseNumber(),
                    sanitizedDto.getZipCode(),
                    sanitizedDto.getCity(),
                    sanitizedDto.getCountry()
            )
            .orElseGet(() -> addressRepository.get().save(addressMapper.get().toNewEntity(sanitizedDto)));
  }

  public boolean hasContent(@Nullable final AddressDto dto) {
    if (dto == null) {
      return false;
    }

    return !ServerStringUtil.safeString(dto.getStreet()).trim().isEmpty()
            || !ServerStringUtil.safeString(dto.getHouseNumber()).trim().isEmpty()
            || !ServerStringUtil.safeString(dto.getZipCode()).trim().isEmpty()
            || !ServerStringUtil.safeString(dto.getCity()).trim().isEmpty()
            || !ServerStringUtil.safeString(dto.getCountry()).trim().isEmpty();
  }

  @Nonnull
  private AddressDto sanitize(@Nonnull final AddressDto dto) {
    final var sanitizedDto = AddressDto.empty();
    sanitizedDto.setId(dto.getId());
    sanitizedDto.setStreet(ServerStringUtil.safeString(dto.getStreet()).trim());
    sanitizedDto.setHouseNumber(ServerStringUtil.safeString(dto.getHouseNumber()).trim());
    sanitizedDto.setZipCode(ServerStringUtil.safeString(dto.getZipCode()).trim());
    sanitizedDto.setCity(ServerStringUtil.safeString(dto.getCity()).trim());
    sanitizedDto.setCountry(ServerStringUtil.safeString(dto.getCountry()).trim());
    return sanitizedDto;
  }
}