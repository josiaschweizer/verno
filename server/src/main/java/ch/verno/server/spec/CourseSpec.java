package ch.verno.server.spec;

import ch.verno.contract.dto.filter.CourseFilter;
import ch.verno.db.entity.course.CourseEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.DayOfWeek;
import java.util.ArrayList;

public class CourseSpec extends BaseSpec<CourseEntity, CourseFilter> {

  @Nonnull
  @Override
  public Specification<CourseEntity> getSpecification(@Nonnull final CourseFilter filter) {
    return (root, query, cb) -> {
      final var predicates = new ArrayList<Predicate>();

      Join<?, ?> scheduleJoin = null;
      Join<?, ?> instructorJoin = null;
      Join<?, ?> levelJoin = null;
      Join<CourseEntity, DayOfWeek> weekdayJoin;

      final var searchText = normalize(filter.searchText());
      if (!searchText.isEmpty()) {
        query.distinct(true);
        final var pattern = "%" + searchText + "%";

        scheduleJoin = root.join("courseSchedule", JoinType.LEFT);
        instructorJoin = root.join("instructor", JoinType.LEFT);
        levelJoin = root.join("courseLevels", JoinType.LEFT);
        weekdayJoin = root.join("weekdays", JoinType.LEFT);

        predicates.add(
                cb.or(
                        likeLower(cb, root.get("title"), pattern),
                        likeLower(cb, root.get("location"), pattern),
                        cb.like(cb.lower(cb.toString(root.get("id"))), pattern),
                        cb.like(cb.lower(cb.toString(root.get("capacity"))), pattern),

                        cb.like(cb.lower(cb.toString(root.get("startTime"))), pattern),
                        cb.like(cb.lower(cb.toString(root.get("endTime"))), pattern),

                        likeLower(cb, scheduleJoin.get("title"), pattern),
                        cb.like(cb.lower(cb.toString(scheduleJoin.get("status"))), pattern),

                        likeLower(cb, instructorJoin.get("firstname"), pattern),
                        likeLower(cb, instructorJoin.get("lastname"), pattern),
                        likeLower(cb, instructorJoin.get("email"), pattern),
                        likeLower(cb, instructorJoin.get("phone"), pattern),

                        likeLower(cb, levelJoin.get("code"), pattern),
                        likeLower(cb, levelJoin.get("name"), pattern),
                        likeLower(cb, levelJoin.get("description"), pattern),
                        cb.like(cb.lower(cb.toString(levelJoin.get("sortingOrder"))), pattern),

                        cb.like(cb.lower(weekdayJoin.as(String.class)), pattern)
                )
        );
      }

      if (filter.instructorId() != null) {
        if (instructorJoin == null) {
          instructorJoin = root.join("instructor", JoinType.LEFT);
        }
        predicates.add(cb.equal(instructorJoin.get("id"), filter.instructorId()));
      }

      if (filter.courseScheduleId() != null) {
        if (scheduleJoin == null) {
          scheduleJoin = root.join("courseSchedule", JoinType.LEFT);
        }
        predicates.add(cb.equal(scheduleJoin.get("id"), filter.courseScheduleId()));
      }

      if (filter.courseLevelId() != null) {
        query.distinct(true);
        if (levelJoin == null) {
          levelJoin = root.join("courseLevels", JoinType.LEFT);
        }
        predicates.add(cb.equal(levelJoin.get("id"), filter.courseLevelId()));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}