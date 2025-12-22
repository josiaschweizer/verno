package ch.verno.ui.lib;

import ch.verno.util.Publ;
import jakarta.annotation.Nonnull;

public class URL {

  public static final String DETAIL = "/detail";

  public static final String PARTICIPANT = "/participant";

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

  private static String getURL(final Class<?> currentClass) {
    final var annotations = currentClass.getAnnotations();

    return Publ.EMPTY_STRING;
  }

}
