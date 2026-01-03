package ch.verno.server.spec;

import ch.verno.common.db.filter.CourseScheduleFilter;
import ch.verno.common.util.Publ;
import ch.verno.db.entity.CourseScheduleEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Locale;

public class CourseScheduleSpec {

  @Nonnull
  public Specification<CourseScheduleEntity> courseScheduleSpec(@Nonnull final CourseScheduleFilter filter) {
    return (root, query, cb) -> {
      final var predicates = new ArrayList<Predicate>();

      Join<CourseScheduleEntity, String> weeksJoin = null;

      final var searchText = normalize(filter.searchText());
      if (!searchText.isEmpty()) {
        query.distinct(true);
        final var pattern = "%" + searchText + "%";

        weeksJoin = root.join("weeks", JoinType.LEFT);

        predicates.add(
                cb.or(
                        likeLower(cb, root.get("title"), pattern),
                        cb.like(cb.lower(cb.toString(root.get("id"))), pattern),
                        cb.like(cb.lower(cb.toString(root.get("status"))), pattern),
                        likeLower(cb, weeksJoin, pattern)
                )
        );
      }

      if (filter.week() != null) {
        query.distinct(true);
        if (weeksJoin == null) {
          weeksJoin = root.join("weeks", JoinType.INNER);
        }

        final var weekAsString = String.valueOf(filter.week());
        predicates.add(cb.equal(weeksJoin, weekAsString));
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