package ch.verno.common.lib.url;

import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;

public class UrlUtil {

  @Nonnull
  public static String buildUrl(@Nonnull final String baseUrl,
                                @Nonnull final String path) {
    final var stringBuilder = new StringBuilder();
    stringBuilder.append(baseUrl);

    if (!baseUrl.endsWith(Publ.SLASH)) {
      stringBuilder.append(Publ.SLASH);
    }
    
    stringBuilder.append(path);

    return stringBuilder.toString();
  }

}
