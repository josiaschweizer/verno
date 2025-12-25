package ch.verno.common.db.service;

import ch.verno.common.db.dto.AddressDto;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface IAddressService {

  @Nonnull
  AddressDto createAddress(@Nonnull final AddressDto addressDto);

  @Nonnull
  AddressDto updateAddress(@Nonnull final AddressDto addressDto);

  @Nonnull
  AddressDto getAddressById(@Nonnull final Long id);

  @Nonnull
  List<AddressDto> getAlAddress();

}
