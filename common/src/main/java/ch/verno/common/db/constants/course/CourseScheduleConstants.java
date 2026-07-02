package ch.verno.common.db.constants.course;

import ch.verno.common.db.constants.base.BaseEntityConstants;
import org.jetbrains.annotations.NonNls;

public class CourseScheduleConstants extends BaseEntityConstants {

  @NonNls public static final String ENTITY_NAME = "courseSchedule";
  @NonNls public static final String MANY_ENTITY_NAME = "courseSchedules";

  @NonNls public static final String TITLE = "title";
  @NonNls public static final String COLOR = "color";
  @NonNls public static final String STATUS = "status";
  @NonNls public static final String WEEKS = "weeks";

  // constants that doesn't exists on the entity
  @NonNls public static final String FIRST_WEEK = "first_week";
  @NonNls public static final String LAST_WEEK = "last_week";

}
