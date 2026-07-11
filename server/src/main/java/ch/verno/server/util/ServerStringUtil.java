package ch.verno.server.util;

import ch.verno.common.dto.ui.phonenumber.PhoneNumber;
import ch.verno.common.lib.sanitize.ObjectSanitizer;
import ch.verno.lib.Publ;
import ch.verno.lib.sanitize.StringSanitizer;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Optional;

public class ServerStringUtil {

  @Nonnull
  public static String safeString(@Nullable final String input) {
    return Optional.ofNullable(StringSanitizer.clean(input)).orElse(Publ.EMPTY_STRING);
  }

  @Nonnull
  public static PhoneNumber safePhone(@Nullable final PhoneNumber phone) {
    return Optional.ofNullable(ObjectSanitizer.clean(phone)).orElseGet(PhoneNumber::empty);
  }

}
