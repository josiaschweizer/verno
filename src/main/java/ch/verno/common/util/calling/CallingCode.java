package ch.verno.common.util.calling;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import jakarta.annotation.Nonnull;

public record CallingCode(
    int countryCode,
    @Nonnull String display,
    @Nonnull String regionCode
) {

  @Nonnull
  public static CallingCode fromString(@Nonnull final String value) {
    final String normalized = value.trim();

    if (!normalized.startsWith("+")) {
      return empty();
    }

    final var phoneNumberUtil = PhoneNumberUtil.getInstance();

    try {
      final var code = Integer.parseInt(normalized.substring(1));
      final var region = phoneNumberUtil.getRegionCodeForCountryCode(code);

      if (region == null) {
        return empty();
      }

      return new CallingCode(
          code,
          "+" + code,
          region
      );
    } catch (NumberFormatException e) {
      return empty();
    }
  }

  @Nonnull
  public static CallingCode empty() {
    return new CallingCode(0, "+", "ZZ");
  }
}