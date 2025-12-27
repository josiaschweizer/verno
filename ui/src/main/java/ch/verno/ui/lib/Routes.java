package ch.verno.ui.lib;

import ch.verno.common.util.Publ;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Arrays;

public class Routes {

  public static final String LOGIN = "login";
  public static final String PARTICIPANTS = "participants";
  public static final String INSTRUCTORS = "instructors";
  public static final String COURSES = "courses";
  public static final String COURSE_SCHEDULES = COURSES + Publ.SLASH + "course-schedules";
  public static final String SETTINGS = "settings";
  public static final String USER_SETTINGS = SETTINGS + Publ.SLASH + "user";
  public static final String MANDANT_SETTINGS = SETTINGS + Publ.SLASH + "mandant";
  public static final String ADMIN_SETTINGS = SETTINGS + Publ.SLASH + "admin";
  public static final String DETAIL = "/detail";

  public static final String VALUE_ACCESSOR_ID = Publ.QUESTION_MARK + Publ.ID + Publ.EQUALS;

  @Nonnull
  public static String createUrlFromBaseUrlSegments(@Nonnull final String... urlSegments) {
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

    return baseURL + Publ.SLASH + id;
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
