package ch.verno.server.spec;

import ch.verno.common.db.filter.CourseLevelFilter;
import ch.verno.common.util.Publ;
import ch.verno.db.entity.CourseLevelEntity;
import jakarta.annotation.Nonnull;
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
        final var code = cb.like(cb.lower(root.get("code")), pattern);
        final var name = cb.like(cb.lower(root.get("name")), pattern);
        final var description = cb.like(cb.lower(root.get("description")), pattern);
        predicates.add(cb.or(code, name, description));
      }

      if (filter.minSortingOrder() != null) {
        predicates.add(cb.greaterThanOrEqualTo(root.get("sortingOrder"), filter.minSortingOrder()));
      }

      if (filter.maxSortingOrder() != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get("sortingOrder"), filter.maxSortingOrder()));
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