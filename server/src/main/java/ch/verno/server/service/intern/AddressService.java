package ch.verno.server.service.intern;

import ch.verno.common.db.dto.table.AddressDto;
import ch.verno.common.db.service.IAddressService;
import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.db.entity.AddressEntity;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.common.tenant.TenantContext;
import ch.verno.server.mapper.AddressMapper;
import ch.verno.server.repository.AddressRepository;
import ch.verno.server.service.helper.ServiceHelper;
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
            TenantEntity.ref(TenantContext.getRequired()),
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
    throw new UnsupportedOperationException("Update address not yet implemented");
  }

  @Nonnull
  @Override
  public AddressDto getAddressById(@Nonnull final Long id) {
    final var foundById = addressRepository.findById(id);
    if (foundById.isEmpty()) {
      throw new DBNotFoundException(DBNotFoundReason.ADDRESS_BY_ID_NOT_FOUND, id);
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

  @Nonnull
  public AddressDto findOrCreateAddress(@Nonnull final AddressDto addressDto) {
    final var street = ServiceHelper.safeString(addressDto.getStreet());
    final var houseNumber = ServiceHelper.safeString(addressDto.getHouseNumber());
    final var zipCode = ServiceHelper.safeString(addressDto.getZipCode());
    final var city = ServiceHelper.safeString(addressDto.getCity());
    final var country = ServiceHelper.safeString(addressDto.getCountry());

    final var existing = addressRepository.findByFields(street, houseNumber, zipCode, city, country);
    if (existing.isPresent()) {
      return AddressMapper.toDto(existing.get());
    }

    final var entity = new AddressEntity(
            TenantEntity.ref(TenantContext.getRequired()),
            street,
            houseNumber,
            zipCode,
            city,
            country
    );
    final var saved = addressRepository.save(entity);
    return AddressMapper.toDto(saved);
  }
}
