package ch.verno.server.spec;

import ch.verno.common.db.constants.course.CourseConstants;
import ch.verno.common.db.constants.course.CourseLevelConstants;
import ch.verno.common.db.constants.course.CourseScheduleConstants;
import ch.verno.common.db.constants.instructor.InstructorConstants;
import ch.verno.contract.dto.filter.CourseFilter;
import ch.verno.db.entity.course.CourseEntity;
import ch.verno.lib.New;
import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.DayOfWeek;

public class CourseSpec extends BaseSpec<CourseEntity, CourseFilter> {

  @Nonnull
  @Override
  public Specification<CourseEntity> getSpecification(@Nonnull final CourseFilter filter) {
    return (root, query, cb) -> {
      final var predicates = New.<Predicate>list();

      Join<?, ?> levelJoin = null;
      Join<?, ?> scheduleJoin = null;
      Join<?, ?> instructorJoin = null;
      Join<CourseEntity, DayOfWeek> weekdayJoin;

      final var searchText = normalize(filter.searchText());
      if (!searchText.isEmpty()) {
        query.distinct(true);
        final var pattern = "%" + searchText + "%";

        levelJoin = root.join(CourseLevelConstants.MANY_ENTITY_NAME, JoinType.LEFT);
        scheduleJoin = root.join(CourseScheduleConstants.ENTITY_NAME, JoinType.LEFT);
        instructorJoin = root.join(InstructorConstants.ENTITY_NAME, JoinType.LEFT);
        weekdayJoin = root.join(CourseConstants.WEEKDAYS, JoinType.LEFT);

        predicates.add(
                cb.or(
                        likeLower(cb, root.get(CourseConstants.TITLE), pattern),
                        likeLower(cb, root.get(CourseConstants.LOCATION), pattern),
                        cb.like(cb.lower(cb.toString(root.get(CourseConstants.ID))), pattern),
                        cb.like(cb.lower(cb.toString(root.get(CourseConstants.CAPACITY))), pattern),

                        cb.like(cb.lower(cb.toString(root.get(CourseConstants.START_TIME))), pattern),
                        cb.like(cb.lower(cb.toString(root.get(CourseConstants.END_TIME))), pattern),

                        likeLower(cb, scheduleJoin.get(CourseConstants.TITLE), pattern),
                        cb.like(cb.lower(cb.toString(scheduleJoin.get(CourseScheduleConstants.STATUS))), pattern),

                        likeLower(cb, instructorJoin.get(InstructorConstants.FIRSTNAME), pattern),
                        likeLower(cb, instructorJoin.get(InstructorConstants.LASTNAME), pattern),
                        likeLower(cb, instructorJoin.get(InstructorConstants.EMAIL), pattern),
                        likeLower(cb, instructorJoin.get(InstructorConstants.PHONE), pattern),

                        likeLower(cb, levelJoin.get(CourseLevelConstants.CODE), pattern),
                        likeLower(cb, levelJoin.get(CourseLevelConstants.NAME), pattern),
                        likeLower(cb, levelJoin.get(CourseLevelConstants.DESCRIPTION), pattern),
                        cb.like(cb.lower(cb.toString(levelJoin.get(CourseLevelConstants.SORTING_ORDER))), pattern),

                        cb.like(cb.lower(weekdayJoin.as(String.class)), pattern)
                )
        );
      }

      if (filter.instructorId() != null) {
        if (instructorJoin == null) {
          instructorJoin = root.join(InstructorConstants.ENTITY_NAME, JoinType.LEFT);
        }
        predicates.add(cb.equal(instructorJoin.get(InstructorConstants.ID), filter.instructorId()));
      }

      if (filter.courseScheduleId() != null) {
        if (scheduleJoin == null) {
          scheduleJoin = root.join(CourseScheduleConstants.ENTITY_NAME, JoinType.LEFT);
        }
        predicates.add(cb.equal(scheduleJoin.get(CourseScheduleConstants.ID), filter.courseScheduleId()));
      }

      if (filter.courseLevelId() != null) {
        query.distinct(true);
        if (levelJoin == null) {
          levelJoin = root.join(CourseLevelConstants.MANY_ENTITY_NAME, JoinType.LEFT);
        }
        predicates.add(cb.equal(levelJoin.get(CourseLevelConstants.ID), filter.courseLevelId()));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  @Nonnull
  @Override
  public String resolveSortProperty(@Nonnull final String property) {
    if (property.equals(CourseScheduleConstants.STATUS)) {
      return createForeignRelation(CourseScheduleConstants.ENTITY_NAME, property);
    }

    return super.resolveSortProperty(property);
  }
}