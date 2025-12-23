package ch.verno.ui.lib;

import ch.verno.common.util.Publ;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Arrays;

public class Routes {

  public static final String PARTICIPANT = "participants";
  public static final String DETAIL = "/detail";

  public static final String VALUE_ACCESSOR_ID = Publ.QUESTION_MARK + "id" + Publ.EQUALS;

  @Nonnull
  public static String getBaseURL(final Class<?> currentClass) {
//    final var annotations = currentClass.getAnnotations();
//    for (final Annotation annotation : annotations) {
//      if (annotation instanceof MenuConfiguration menuConfiguration) {
//        return menuConfiguration.getBaseUrl();
//      }
//    }
    return Publ.EMPTY_STRING;
  }

  @Nonnull
  public static String getDetailURL(@Nonnull final Class<?> currentClass) {
    return getURL(currentClass) + DETAIL;
  }

  public static String getURLWithId(@Nonnull final String baseURL,
                                    @Nullable final Long id) {
    if (id == null) {
      return Publ.EMPTY_STRING;
    }

    return baseURL + VALUE_ACCESSOR_ID + id;
  }

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
