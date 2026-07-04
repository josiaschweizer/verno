package ch.verno.server.bo.table.address;

import ch.verno.contract.dto.table.address.AddressDto;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.sanitize.address.AddressSanitizer;
import ch.verno.server.service.entity.address.AddressService;
import ch.verno.server.util.ServerStringUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AddressBo {

  @Nonnull private final Lazy<AddressService> addressService;

  protected AddressBo(@Nonnull final ServerBean bean) {
    this.addressService = Lazy.of(() -> bean.get(AddressService.class));
  }

  @Nonnull
  public AddressDto findOrCreate(@Nonnull final AddressDto dto) {
    if (!hasContent(dto)) {
      return AddressDto.empty();
    }

    final var sanitizedDto = AddressSanitizer.sanitize(dto);
    return findByFields(sanitizedDto).orElseGet(() -> addressService.get().save(sanitizedDto));
  }

  @Nonnull
  public Optional<AddressDto> findByFields(@Nonnull final AddressDto dto) {
    return addressService.get().findByFields(
            dto.getStreet(),
            dto.getHouseNumber(),
            dto.getZipCode(),
            dto.getCity(),
            dto.getCountry()
    );
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
}