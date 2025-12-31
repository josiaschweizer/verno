package ch.verno.server.service;

import ch.verno.common.db.dto.AddressDto;
import ch.verno.common.db.service.IAddressService;
import ch.verno.common.exceptions.NotFoundException;
import ch.verno.common.exceptions.NotFoundReason;
import ch.verno.db.entity.AddressEntity;
import ch.verno.server.mapper.AddressMapper;
import ch.verno.server.repository.AddressRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService implements IAddressService {

  @Nonnull
  private final AddressRepository addressRepository;

  public AddressService(@Nonnull final AddressRepository addressRepository) {
    this.addressRepository = addressRepository;
  }

  @Nonnull
  @Override
  public AddressDto createAddress(@Nonnull final AddressDto addressDto) {
    final var entity = new AddressEntity(
            ServiceHelper.safeString(addressDto.getStreet()),
            ServiceHelper.safeString(addressDto.getHouseNumber()),
            ServiceHelper.safeString(addressDto.getZipCode()),
            ServiceHelper.safeString(addressDto.getCity()),
            ServiceHelper.safeString(addressDto.getCountry())
    );

    final var saved = addressRepository.save(entity);
    return AddressMapper.toDto(saved);
  }

  @Nonnull
  @Override
  public AddressDto updateAddress(@Nonnull final AddressDto addressDto) {
    return null;
  }

  @Nonnull
  @Override
  public AddressDto getAddressById(@Nonnull final Long id) {
    final var foundById = addressRepository.findById(id);
    if (foundById.isEmpty()) {
      throw new NotFoundException(NotFoundReason.ADDRESS_BY_ID_NOT_FOUND, id);
    }

    return AddressMapper.toDto(foundById.get());
  }

  @Nonnull
  @Override
  public List<AddressDto> getAddresses() {
    return addressRepository.findAll()
            .stream()
            .map(AddressMapper::toDto)
            .toList();
  }
}
