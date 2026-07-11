package ch.verno.server.sanitize.participant;

import ch.verno.contract.dto.table.participant.ParentDto;
import ch.verno.server.sanitize.address.AddressSanitizer;
import ch.verno.server.sanitize.gender.GenderSanitizer;
import ch.verno.server.util.ServerStringUtil;
import jakarta.annotation.Nonnull;

public class ParentSanitizer {

  @Nonnull
  public static ParentDto sanitize(@Nonnull final ParentDto dto) {
    final var sanitized = ParentDto.empty();

    sanitized.setId(dto.getId());
    sanitized.setTenantId(dto.getTenantId());

    sanitized.setFirstName(ServerStringUtil.safeString((dto.getFirstName())));
    sanitized.setLastName(ServerStringUtil.safeString((dto.getLastName())));
    sanitized.setEmail(ServerStringUtil.safeString((dto.getEmail())));
    sanitized.setPhone(ServerStringUtil.safePhone(dto.getPhone()));

    sanitized.setGender(GenderSanitizer.sanitize(dto.getGender()));
    sanitized.setAddress(AddressSanitizer.sanitize(dto.getAddress()));

    return sanitized;
  }

}
