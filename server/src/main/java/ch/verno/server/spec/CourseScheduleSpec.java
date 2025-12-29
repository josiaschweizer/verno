package ch.verno.server.spec;

import ch.verno.common.db.filter.CourseScheduleFilter;
import ch.verno.common.util.Publ;
import ch.verno.db.entity.CourseScheduleEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Locale;

public class CourseScheduleSpec {

  @Nonnull
  public Specification<CourseScheduleEntity> courseScheduleSpec(@Nonnull final CourseScheduleFilter filter) {
    return (root, query, cb) -> {
      final var predicates = new ArrayList<Predicate>();

      final var searchText = normalize(filter.searchText());
      if (!searchText.isEmpty()) {
        query.distinct(true);
        final var pattern = "%" + searchText + "%";

        final var name = cb.like(cb.lower(root.get("title")), pattern);

        var weeksPredicate = cb.conjunction();
        try {
          final var weekJoin = root.join("weeks", JoinType.LEFT);
          weeksPredicate = cb.like(cb.lower(weekJoin.as(String.class)), pattern);
        } catch (IllegalArgumentException ignored) {
          // illegal argument exception gets ignored rn
        }

        predicates.add(cb.or(name, weeksPredicate));
      }

      if (filter.week() != null) {
        final var weekJoin = root.join("weeks", JoinType.INNER);
        predicates.add(cb.equal(weekJoin, filter.week()));
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