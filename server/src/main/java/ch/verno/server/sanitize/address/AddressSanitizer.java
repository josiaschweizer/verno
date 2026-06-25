package ch.verno.server.sanitize.address;

import ch.verno.contract.dto.table.address.AddressDto;
import ch.verno.server.util.ServerStringUtil;
import jakarta.annotation.Nonnull;

public class AddressSanitizer {

  @Nonnull
  public static AddressDto sanitize(@Nonnull final AddressDto dto) {
    final var sanitized = AddressDto.empty();

    sanitized.setId(dto.getId());
    sanitized.setStreet(ServerStringUtil.safeString(dto.getStreet()));
    sanitized.setHouseNumber(ServerStringUtil.safeString(dto.getHouseNumber()));
    sanitized.setZipCode(ServerStringUtil.safeString(dto.getZipCode()));
    sanitized.setCity(ServerStringUtil.safeString(dto.getCity()));
    sanitized.setCountry(ServerStringUtil.safeString(dto.getCountry()));

    return sanitized;
  }

}
