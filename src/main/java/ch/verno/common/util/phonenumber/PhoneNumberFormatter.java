package ch.verno.common.util.phonenumber;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber;

public final class PhoneNumberFormatter {

  private static final PhoneNumberUtil UTIL = PhoneNumberUtil.getInstance();

  private PhoneNumberFormatter() {
  }

  public static String formatNational(String digits, String region) {
    try {
      final Phonenumber.PhoneNumber number =
          UTIL.parse(digits, region);
      return UTIL.format(number, PhoneNumberFormat.NATIONAL);
    } catch (Exception e) {
      return digits;
    }
  }

  public static boolean isValid(String digits, String region) {
    try {
      final Phonenumber.PhoneNumber number =
          UTIL.parse(digits, region);
      return UTIL.isValidNumber(number);
    } catch (Exception e) {
      return false;
    }
  }
}