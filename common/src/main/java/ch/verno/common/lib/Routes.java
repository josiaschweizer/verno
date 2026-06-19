package ch.verno.common.lib;

import ch.verno.lib.Publ;
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
  @NonNls public static final String MAIL = "mail";
  @NonNls public static final String MAIL_LOG = MAIL + "/log";
  @NonNls public static final String MAIL_TEST = MAIL + "/test";

  @NonNls public static final String DETAIL = "/detail";

  @NonNls public static final String TENANT_NOT_FOUND = "mandant-not-found";

  public static final String VALUE_ACCESSOR_ID = Publ.QUESTION_MARK + Publ.ID + Publ.EQUALS;

}
