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
    final var trimmed = value.trim();

    if (trimmed.isEmpty()) {
      return empty();
    }

    final var normalized = trimmed.replaceAll("\\s+", "");

    final String digits;
    if (normalized.startsWith(Publ.PLUS)) {
      digits = normalized.substring(1).replaceAll("\\D+", "");
    } else if (normalized.startsWith("00")) {
      digits = normalized.substring(2).replaceAll("\\D+", "");
    } else {
      final var nationalDigits = normalized.replaceAll("\\D+", "");
      if (nationalDigits.isEmpty()) {
        return empty();
      }
      final var national = nationalDigits.startsWith("0")
          ? nationalDigits.substring(1)
          : nationalDigits;

      return national.isEmpty()
          ? empty()
          : new PhoneNumber(CallingCode.defaultSwiss(), national);
    }

    if (digits.isEmpty()) {
      return empty();
    }

    final var allCodesDesc = ch.verno.common.util.calling.CallingCodeHelper.getCallingCodes().stream()
        .sorted((a, b) -> Integer.compare(b.countryCode(), a.countryCode()))
        .toList();

    for (final var code : allCodesDesc) {
      final var codeDigits = String.valueOf(code.countryCode());
      if (digits.startsWith(codeDigits)) {
        final var numberPart = digits.substring(codeDigits.length());
        if (numberPart.isEmpty()) {
          return empty();
        }
        return new PhoneNumber(code, numberPart);
      }
    }

    final var chDigits = String.valueOf(CallingCode.defaultSwiss().countryCode());
    if (digits.startsWith(chDigits)) {
      final var numberPart = digits.substring(chDigits.length());
      return numberPart.isEmpty()
          ? empty()
          : new PhoneNumber(CallingCode.defaultSwiss(), numberPart);
    }

    return empty();
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
  public static PhoneNumber swissEmpty() {
    return new PhoneNumber(
            CallingCode.defaultSwiss(),
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