package ch.verno.common.util.calling;

import ch.verno.common.util.Publ;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CallingCodeHelper {

  @Nonnull
  public static List<CallingCode> getCallingCodes() {
    final var phoneNumberUtil = PhoneNumberUtil.getInstance();
    final var countryCodeToRegion = new TreeMap<Integer, String>();

    for (final var region : phoneNumberUtil.getSupportedRegions()) {
      final int countryCode = phoneNumberUtil.getCountryCodeForRegion(region);

      if (countryCode <= 0) {
        continue;
      }

      countryCodeToRegion.putIfAbsent(countryCode, region);
    }

    return countryCodeToRegion.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .map(entry -> new CallingCode(
            entry.getKey(),
            Publ.PLUS + entry.getKey(),
            entry.getValue()
        ))
        .toList();
  }
}