package ch.verno.publ;

import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NonNls;

import java.util.Arrays;

@SuppressWarnings("HardcodedFileSeparator")
public class Routes {

  @NonNls public static final String LOGIN = "login";
  @NonNls public static final String PARTICIPANTS = "participants";
  @NonNls public static final String INSTRUCTORS = "instructors";
  @NonNls public static final String COURSE = "course";
  @NonNls public static final String COURSES = COURSE + "/courses";
  @NonNls public static final String COURSE_LEVELS = "course-levels";
  @NonNls public static final String COURSE_SCHEDULES = COURSE + "/course-schedules";
  @NonNls public static final String IO = "io";
  @NonNls public static final String SETTINGS = "settings";
  @NonNls public static final String USER_SETTINGS = SETTINGS + "/user";
  @NonNls public static final String TENANT_SETTINGS = SETTINGS + "/mandant";
  @NonNls public static final String APP_USERS = "app-users";
  @NonNls public static final String MAIL_LOG = "mail-log";

  @NonNls public static final String DETAIL = "/detail";

  @NonNls public static final String TENANT_NOT_FOUND = "mandant-not-found";

  public static final String VALUE_ACCESSOR_ID = Publ.QUESTION_MARK + Publ.ID + Publ.EQUALS;

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
    return getURL(currentClass) + DETAIL;
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
