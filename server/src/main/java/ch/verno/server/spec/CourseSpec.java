package ch.verno.server.spec;

import ch.verno.common.db.filter.CourseFilter;
import ch.verno.common.util.Publ;
import ch.verno.db.entity.CourseEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Locale;

public class CourseSpec {

  @Nonnull
  public Specification<CourseEntity> courseSpec(@Nonnull final CourseFilter filter) {
    return (root, query, cb) -> {
      final var predicates = new ArrayList<Predicate>();

      Join<?, ?> scheduleJoin = null;
      Join<?, ?> instructorJoin = null;
      Join<?, ?> levelJoin = null;
      Join<CourseEntity, DayOfWeek> weekdayJoin = null;

      final var searchText = normalize(filter.searchText());
      if (!searchText.isEmpty()) {
        query.distinct(true);
        final var pattern = "%" + searchText + "%";

        scheduleJoin = root.join("schedule", JoinType.LEFT);
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
          scheduleJoin = root.join("schedule", JoinType.LEFT);
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

  @Nonnull
  private static Predicate likeLower(@Nonnull final CriteriaBuilder cb,
                                     @Nonnull final Expression<?> path,
                                     @Nonnull final String pattern) {
    return cb.like(cb.lower(cb.coalesce(path.as(String.class), Publ.EMPTY_STRING)), pattern);
  }

  @Nonnull
  private static String normalize(@Nullable final String s) {
    if (s == null) {
      return Publ.EMPTY_STRING;
    }
    return s.trim().toLowerCase(Locale.ROOT);
  }
}