package ch.verno.ui.lib.url;

import ch.verno.common.lib.Routes;
import ch.verno.lib.Publ;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Arrays;

public class RoutesUtil {

  @Nonnull
  public static String createUrlFromUrlSegments(@Nonnull final String... urlSegments) {
    final var stringBuilder = new StringBuilder();

    for (var segment : urlSegments) {
      if (!stringBuilder.isEmpty() && !stringBuilder.toString().endsWith(Publ.SLASH)) {
        stringBuilder.append(Publ.SLASH);
      }
      segment = segment.replaceAll("^/+", "").replaceAll("/+$", "");
      stringBuilder.append(segment);
    }

    return stringBuilder.toString();
  }

  @Nonnull
  public static String getDetailURL(@Nonnull final Class<?> currentClass) {
    return getURL(currentClass) + Routes.DETAIL;
  }

  public static String getURLWithId(@Nonnull final String baseURL,
                                    @Nullable final Long id) {
    if (id == null) {
      return Publ.EMPTY_STRING;
    }

    return baseURL + Publ.SLASH + id;
  }

  @Nonnull
  private static String getURL(final Class<?> currentClass) {
    final var annotations = Arrays.stream(currentClass.getAnnotations()).toList();
    for (final var annotation : annotations) {
      if (annotation instanceof Route routeAnnotation) {
        return routeAnnotation.value();
      }
    }

    return Publ.EMPTY_STRING;
  }

}
