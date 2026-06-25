package ch.verno.server.bo.table.address;

import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.contract.dto.table.address.AddressDto;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.sanitize.address.AddressSanitizer;
import ch.verno.server.service.intern.table.address.AddressService;
import ch.verno.server.util.ServerStringUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

@Component
public class AddressBo {

  @Nonnull private final Lazy<AddressService> addressService;

  protected AddressBo(@Nonnull final ServerBean bean) {
    this.addressService = Lazy.of(() -> bean.get(AddressService.class));
  }

  @Nullable
  public AddressDto saveOrUpdate(@Nullable final AddressDto dto) {
    if (dto == null || !hasContent(dto)) {
      return null;
    }

    final var sanitizedDto = AddressSanitizer.sanitize(dto);

    if (sanitizedDto.getId() != null && sanitizedDto.getId() != 0L) {
      addressService.get()
              .findById(sanitizedDto.getId())
              .orElseThrow(() -> new DBNotFoundException(
                      DBNotFoundReason.ADDRESS_BY_ID_NOT_FOUND,
                      sanitizedDto.getId()
              ));
    }

    return addressService.get().save(sanitizedDto);
  }

  @Nonnull
  public AddressDto findOrCreate(@Nonnull final AddressDto dto) {
    final var sanitizedDto = AddressSanitizer.sanitize(dto);

    final var foundOptional = addressService.get().findByFields(
                    sanitizedDto.getStreet(),
                    sanitizedDto.getHouseNumber(),
                    sanitizedDto.getZipCode(),
                    sanitizedDto.getCity(),
                    sanitizedDto.getCountry()
    );
    return foundOptional.orElseGet(() -> addressService.get().save(sanitizedDto));
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