package ch.verno.common.util.phonenumber;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;

public final class PhoneNumberFormatter {

  private static final PhoneNumberUtil UTIL = PhoneNumberUtil.getInstance();

  private PhoneNumberFormatter() {
  }

  public static String formatNational(final String digits, final String region) {
    try {
      final var number = UTIL.parse(digits, region);
      return UTIL.format(number, PhoneNumberFormat.NATIONAL);
    } catch (Exception e) {
      return digits;
    }
  }

  public static String formatInternationalWithoutCallingCode(final String digits,
                                                             final String region,
                                                             final int countryCode) {
    try {
      final var number = UTIL.parse(digits, region);
      final var international = UTIL.format(number, PhoneNumberFormat.INTERNATIONAL);

      final var prefix = "+" + countryCode;
      var out = international;

      if (out.startsWith(prefix)) {
        out = out.substring(prefix.length()).trim();
      }

      out = out.replaceFirst("^\\(0\\)\\s*", "");
      return out;
    } catch (Exception e) {
      return digits;
    }
  }

  public static boolean isValid(final String digits, final String region) {
    try {
      final var number = UTIL.parse(digits, region);
      return UTIL.isValidNumber(number);
    } catch (Exception e) {
      return false;
    }
  }
}