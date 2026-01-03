package ch.verno.server.spec;

import ch.verno.common.db.filter.CourseLevelFilter;
import ch.verno.common.util.Publ;
import ch.verno.db.entity.CourseLevelEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Locale;

public class CourseLevelSpec {

  @Nonnull
  public Specification<CourseLevelEntity> courseLevelSpec(@Nonnull final CourseLevelFilter filter) {
    return (root, query, cb) -> {
      final var predicates = new ArrayList<Predicate>();

      final var searchText = normalize(filter.searchText());
      if (!searchText.isEmpty()) {
        final var pattern = "%" + searchText + "%";

        predicates.add(
                cb.or(
                        likeLower(cb, root.get("code"), pattern),
                        likeLower(cb, root.get("name"), pattern),
                        likeLower(cb, root.get("description"), pattern),

                        cb.like(cb.lower(cb.toString(root.get("id"))), pattern),
                        cb.like(cb.lower(cb.toString(root.get("sortingOrder"))), pattern)
                )
        );
      }

      if (filter.minSortingOrder() != null) {
        predicates.add(cb.greaterThanOrEqualTo(
                root.get("sortingOrder"),
                toInteger(filter.minSortingOrder())
        ));
      }

      if (filter.maxSortingOrder() != null) {
        predicates.add(cb.lessThanOrEqualTo(
                root.get("sortingOrder"),
                toInteger(filter.maxSortingOrder())
        ));
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

  @Nullable
  private static Integer toInteger(@Nullable final Long value) {
    if (value == null) {
      return null;
    }
    if (value > Integer.MAX_VALUE) {
      return Integer.MAX_VALUE;
    }
    if (value < Integer.MIN_VALUE) {
      return Integer.MIN_VALUE;
    }
    return value.intValue();
  }

  @Nonnull
  private static String normalize(@Nullable final String s) {
    if (s == null) {
      return Publ.EMPTY_STRING;
    }
    return s.trim().toLowerCase(Locale.ROOT);
  }
}