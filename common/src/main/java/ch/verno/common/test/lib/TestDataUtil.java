package ch.verno.common.test.lib;

import org.jetbrains.annotations.NonNls;

public class TestDataUtil {

  @NonNls public static final String PARTICIPANT_FIRSTNAME = "Firstname";
  @NonNls public static final String PARTICIPANT_LASTNAME = "Lastname";

  @NonNls public static final String ADDRESS_STREET = "Samplestreet";
  @NonNls public static final String ADDRESS_HOUSE_NUMBER = "1234a";
  @NonNls public static final String ADDRESS_ZIP_CODE = "9000";
  @NonNls public static final String ADDRESS_CITY = "St. Gallen";
  @NonNls public static final String ADDRESS_COUNTRY = "Switzerland";

  @NonNls public static final String PARENT_ONE_FIRSTNAME = "Parent One Firstname";
  @NonNls public static final String PARENT_ONE_LASTNAME = "Parent One Lastname";
  @NonNls public static final String PARENT_ONE_MAIL = "parent.one@testmail.com";
  @NonNls public static final String PARENT_ONE_PHONE = "077 777 77 77";

  @NonNls public static final String PARENT_TWO_FIRSTNAME = "Parent Two Firstname";
  @NonNls public static final String PARENT_TWO_LASTNAME = "Parent Two Lastname";
  @NonNls public static final String PARENT_TWO_MAIL = "parent.two@testmail.com";
  @NonNls public static final String PARENT_TWO_PHONE = "077 777 77 76";

  @NonNls public static final String INSTRUCTOR_FIRSTNAME = "Instructor Firstname";
  @NonNls public static final String INSTRUCTOR_LASTNAME = "Instructor Lastname";
  @NonNls public static final String INSTRUCTOR_MAIL = "instructor@mail.com";
  @NonNls public static final String INSTRUCTOR_PHONE = "077 777 77 75";

  @NonNls public static final String COURSE_SCHEDULE_TITLE = "Course Schedule Title";

  @NonNls public static final String COURSE_TITLE = "Course Title";
  @NonNls public static final String COURSE_LOCATION = "Course Location";
  @NonNls public static final String COURSE_NOTE = "Demo Course Note";

  @NonNls public static final String COURSE_LEVEL_CODE = "CourseLevel Code";
  @NonNls public static final String COURSE_LEVEL_NAME = "Demo Course Level Name";
  @NonNls public static final String COURSE_LEVEL_DESCRIPTION = "Thats my Description for my Course Level Dto";

  private static final int DEFAULT_YEAR_WEEK_COUNT = 8;
  private static final int DEFAULT_DAY_OF_WEEK_COUNT = 7;

  private static final int PARTICIPANT_AGE = 10;
  private static final int COURSE_MAX_PARTICIPANTS = 14;

  private static final int COURSE_START_HOUR = 19;
  private static final int COURSE_START_MINUTE = 0;
  private static final int COURSE_END_HOUR = 21;
  private static final int COURSE_END_MINUTE = 0;

  private TestDataUtil() {
    // utility class
  }

//  @Nonnull
//  public static ParticipantDto createDemoParticipant(@Nonnull final String recipient) {
//    return new ParticipantDto(
//            null,
//            PARTICIPANT_FIRSTNAME,
//            PARTICIPANT_LASTNAME,
//            LocalDate.now().minusYears(PARTICIPANT_AGE),
//            createDemoGender(Gender.MALE),
//            recipient,
//            PhoneNumber.empty(),
//            Publ.EMPTY_STRING,
//            true,
//            New.list(),
//            New.list(),
//            createDemoAddress(),
//            createDemoParentOne(),
//            createDemoParentTwo(),
//            New.list()
//    );
//  }
//
//  @Nonnull
//  private static GenderDto createDemoGender(@Nonnull final Gender gender) {
//    return new GenderDto(
//            null,
//            gender.getInternalName(),
//            Publ.EMPTY_STRING,
//            New.map()
//    );
//  }
//
//  @Nonnull
//  private static AddressDto createDemoAddress() {
//    return new AddressDto(
//            null,
//            ADDRESS_STREET,
//            ADDRESS_HOUSE_NUMBER,
//            ADDRESS_ZIP_CODE,
//            ADDRESS_CITY,
//            ADDRESS_COUNTRY
//    );
//  }
//
//  @Nonnull
//  private static ParentDto createDemoParentOne() {
//    return new ParentDto(
//            null,
//            PARENT_ONE_FIRSTNAME,
//            PARENT_ONE_LASTNAME,
//            PARENT_ONE_MAIL,
//            PhoneNumber.fromString(PARENT_ONE_PHONE),
//            createDemoGender(Gender.MALE),
//            createDemoAddress()
//    );
//  }
//
//  @Nonnull
//  private static ParentDto createDemoParentTwo() {
//    return new ParentDto(
//            null,
//            PARENT_TWO_FIRSTNAME,
//            PARENT_TWO_LASTNAME,
//            PARENT_TWO_MAIL,
//            PhoneNumber.fromString(PARENT_TWO_PHONE),
//            createDemoGender(Gender.FEMALE),
//            createDemoAddress()
//    );
//  }
//
//  @Nonnull
//  public static InstructorDto createDemoInstructor() {
//    return new InstructorDto(
//            null,
//            INSTRUCTOR_FIRSTNAME,
//            INSTRUCTOR_LASTNAME,
//            INSTRUCTOR_MAIL,
//            PhoneNumber.fromString(INSTRUCTOR_PHONE),
//            createDemoGender(Gender.MALE),
//            createDemoAddress()
//    );
//  }
//
//  @Nonnull
//  public static CourseScheduleDto createDemoCourseSchedule() {
//    return createDemoCourseSchedule(CourseScheduleStatus.PLANNED);
//  }
//
//  @Nonnull
//  public static CourseScheduleDto createDemoCourseSchedule(@Nonnull final CourseScheduleStatus status) {
//    return new CourseScheduleDto(
//            null,
//            COURSE_SCHEDULE_TITLE,
//            Colors.PRIMARY_COLOR,
//            status,
//            createDemoYearWeeks()
//    );
//  }
//
//  @Nonnull
//  public static CourseDto createDemoCourse() {
//    return new CourseDto(
//            null,
//            COURSE_TITLE,
//            COURSE_MAX_PARTICIPANTS,
//            COURSE_LOCATION,
//            New.list(createDemoCourseLevel()),
//            createDemoCourseSchedule(),
//            createDemoDayOfWeeks(),
//            LocalTime.of(COURSE_START_HOUR, COURSE_START_MINUTE),
//            LocalTime.of(COURSE_END_HOUR, COURSE_END_MINUTE),
//            createDemoInstructor(),
//            New.list(),
//            COURSE_NOTE,
//            Colors.GRAY
//    );
//  }
//
//  @Nonnull
//  public static CourseLevelDto createDemoCourseLevel() {
//    return new CourseLevelDto(
//            null,
//            COURSE_LEVEL_CODE,
//            COURSE_LEVEL_NAME,
//            COURSE_LEVEL_DESCRIPTION,
//            null
//    );
//  }
//
//  @Nonnull
//  public static List<YearWeekDto> createDemoYearWeeks() {
//    return createDemoYearWeeks(DEFAULT_YEAR_WEEK_COUNT);
//  }
//
//  @Nonnull
//  public static List<YearWeekDto> createDemoYearWeeks(final int count) {
//    final var list = New.<YearWeekDto>list();
//    final var today = LocalDate.now();
//    final var weekFields = WeekFields.ISO;
//
//    for (int i = 0; i < count; i++) {
//      final var date = today.plusWeeks(i);
//      final var year = date.get(weekFields.weekBasedYear());
//      final var week = date.get(weekFields.weekOfWeekBasedYear());
//
//      list.add(new YearWeekDto(year, week));
//    }
//
//    return list;
//  }
//
//  @Nonnull
//  public static List<DayOfWeek> createDemoDayOfWeeks() {
//    return createDemoDayOfWeeks(DEFAULT_DAY_OF_WEEK_COUNT);
//  }
//
//  @Nonnull
//  public static List<DayOfWeek> createDemoDayOfWeeks(final int count) {
//    final var list = New.<DayOfWeek>list();
//    final var today = LocalDate.now();
//
//    for (int i = 0; i < count; i++) {
//      final var date = today.plusDays(i);
//      list.add(date.getDayOfWeek());
//    }
//
//    return list;
//  }
}