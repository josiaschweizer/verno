package ch.verno.common.lib.sanitize;

import ch.verno.common.dto.ui.phonenumber.PhoneNumber;
import ch.verno.common.lib.calling.CallingCode;
import ch.verno.lib.sanitize.StringSanitizer;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class ObjectSanitizer {

  @Nullable
  public static PhoneNumber clean(@Nullable final PhoneNumber input) {
    if (input == null) {
      return null;
    }

    return cleanNullSave(input);
  }

  @Nonnull
  public static PhoneNumber cleanNullSave(@Nullable final PhoneNumber phone) {
    if (phone == null) {
      return PhoneNumber.empty();
    }

    final var calling = phone.callingCode();

    final var cleanDisplay = StringSanitizer.cleanNullSave(calling.display());
    final var cleanRegionCode = StringSanitizer.cleanNullSave(calling.regionCode());

    final var sanitizedCallingCode = new CallingCode(
            calling.countryCode(),
            cleanDisplay,
            cleanRegionCode
    );
    final var phoneNumber = StringSanitizer.cleanNullSave(phone.phoneNumber());

    return new PhoneNumber(sanitizedCallingCode, phoneNumber);
  }

}
