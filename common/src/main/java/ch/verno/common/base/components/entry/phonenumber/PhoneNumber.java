package ch.verno.common.base.components.entry.phonenumber;

import ch.verno.common.util.Publ;
import ch.verno.common.util.calling.CallingCode;
import jakarta.annotation.Nonnull;

public record PhoneNumber(
    @Nonnull CallingCode callingCode,
    @Nonnull String phoneNumber
) {

  @Nonnull
  public static PhoneNumber fromString(@Nonnull final String value) {
    final String trimmed = value.trim();

    if (trimmed.isEmpty()) {
      return empty();
    }

    final int firstSpace = trimmed.indexOf(Publ.SPACE);
    if (firstSpace < 0) {
      return empty();
    }

    final String codePart = trimmed.substring(0, firstSpace);
    final String numberPart = trimmed.substring(firstSpace + 1).replaceAll("\\s+", "");

    return new PhoneNumber(
        CallingCode.fromString(codePart),
        numberPart
    );
  }

  public boolean isEmpty() {
    return callingCode.countryCode() == 0 || phoneNumber.isEmpty();
  }

  @Nonnull
  public static PhoneNumber empty() {
    return new PhoneNumber(
        CallingCode.empty(),
        Publ.EMPTY_STRING
    );
  }

  @Nonnull
  @Override
  public String toString() {
    if (callingCode.countryCode() == 0 || phoneNumber.isEmpty()) {
      return Publ.EMPTY_STRING;
    }
    return callingCode.display() + Publ.SPACE + phoneNumber;
  }
}