package ch.verno.server.spec;

import ch.verno.common.db.filter.CourseFilter;
import ch.verno.common.util.Publ;
import ch.verno.db.entity.CourseEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Locale;

public class CourseSpec {

  //TODO noch mit allen möglichen filter optionen erweitern

  @Nonnull
  public Specification<CourseEntity> courseSpec(@Nonnull final CourseFilter filter) {
    return (root, query, cb) -> {
      final var predicates = new ArrayList<Predicate>();

      final var searchText = normalize(filter.searchText());
      if (!searchText.isEmpty()) {
        query.distinct(true);

        final var pattern = "%" + searchText + "%";

        final var title = cb.like(cb.lower(root.get("title")), pattern);
        final var location = cb.like(cb.lower(root.get("location")), pattern);

        final var scheduleJoin = root.join("schedule", JoinType.LEFT);
        final var scheduleName = cb.like(cb.lower(scheduleJoin.get("title")), pattern);

        final var instructorJoin = root.join("instructor", JoinType.LEFT);
        final var instrFirst = cb.like(cb.lower(instructorJoin.get("firstname")), pattern);
        final var instrLast = cb.like(cb.lower(instructorJoin.get("lastname")), pattern);

        final var levelJoin = root.join("courseLevels", JoinType.LEFT);
        final var levelName = cb.like(cb.lower(levelJoin.get("setting.name")), pattern);
        final var levelCode = cb.like(cb.lower(levelJoin.get("setting.code")), pattern);

        predicates.add(cb.or(title, location, scheduleName, instrFirst, instrLast, levelName, levelCode));
      }

      if (filter.instructorId() != null) {
        predicates.add(cb.equal(root.get("instructor").get("id"), filter.instructorId()));
      }

      if (filter.courseScheduleId() != null) {
        predicates.add(cb.equal(root.get("schedule").get("id"), filter.courseScheduleId()));
      }

      if (filter.courseLevelId() != null) {
        final var levelJoin = root.join("courseLevels", JoinType.INNER);
        predicates.add(cb.equal(levelJoin.get("id"), filter.courseLevelId()));
        query.distinct(true);
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  @Nonnull
  private static String normalize(final String s) {
    if (s == null) {
      return Publ.EMPTY_STRING;
    }
    return s.trim().toLowerCase(Locale.ROOT);
  }
}