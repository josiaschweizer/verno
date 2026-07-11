package ch.verno.common.lib.url;

import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;

public class UrlUtil {

  @Nonnull
  public static String buildSafeUrl(@Nonnull final String baseUrl,
                                    @Nonnull final String path) {
    if (baseUrl.isBlank()) {
      return path;
    } else if (path.isBlank()) {
      return baseUrl;
    }

    return buildUrl(baseUrl, path);
  }

  @Nonnull
  public static String buildUrl(@Nonnull final String baseUrl,
                                @Nonnull final String path) {
    final var stringBuilder = new StringBuilder();
    stringBuilder.append(baseUrl);

    if (!baseUrl.endsWith(Publ.SLASH) && !path.startsWith(Publ.SLASH)) {
      stringBuilder.append(Publ.SLASH);
    }
    
    stringBuilder.append(path);

    return stringBuilder.toString();
  }

}
