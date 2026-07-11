package ch.verno.contract.mail.placeholder.course;

import ch.verno.contract.mail.placeholder.base.BasePlaceholderMapping;
import ch.verno.lib.New;
import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;

import java.util.Map;
import java.util.function.Function;

public class CourseMailPlaceholderMapping extends BasePlaceholderMapping<CourseMailPlaceholderContext, CoursePlaceholder> {

  @Nonnull
  @Override
  public Map<CoursePlaceholder, Function<CourseMailPlaceholderContext, String>> getMapping() {
    final var mapping = New.<CoursePlaceholder, Function<CourseMailPlaceholderContext, String>>map();

    mapping.put(CoursePlaceholder.FIRSTNAME, ctx -> ctx.participant().getFirstName());
    mapping.put(CoursePlaceholder.LASTNAME, ctx -> ctx.participant().getLastName());
    mapping.put(CoursePlaceholder.COURSE_NAME, ctx -> ctx.course() != null ? ctx.course().getTitle() : Publ.EMPTY_STRING);
    mapping.put(CoursePlaceholder.COURSE_START_DATE, CourseMailPlaceholderContext::getCourseStartDate);
    mapping.put(CoursePlaceholder.COURSE_END_DATE, CourseMailPlaceholderContext::getCourseEndDate);
    mapping.put(CoursePlaceholder.COURSE_START_TIME, CourseMailPlaceholderContext::getCourseStartTime);
    mapping.put(CoursePlaceholder.COURSE_END_TIME, CourseMailPlaceholderContext::getCourseEndTime);


    return mapping;
  }
}