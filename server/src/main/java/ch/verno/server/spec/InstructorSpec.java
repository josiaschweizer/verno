package ch.verno.server.spec;

import ch.verno.common.db.filter.InstructorFilter;
import ch.verno.common.util.Publ;
import ch.verno.db.entity.InstructorEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Locale;

public class InstructorSpec {

  @Nonnull
  public Specification<InstructorEntity> instructorSpec(@Nonnull final InstructorFilter filter) {
    return (root, query, cb) -> {
      final var predicates = new ArrayList<Predicate>();

      Join<?, ?> addressJoin;
      Join<?, ?> genderJoin = null;

      final var searchText = normalize(filter.searchText());
      if (!searchText.isEmpty()) {
        query.distinct(true);

        final var pattern = "%" + searchText + "%";

        addressJoin = root.join("address", JoinType.LEFT);
        genderJoin = root.join("gender", JoinType.LEFT);

        predicates.add(
                cb.or(
                        likeLower(cb, root.get("firstname"), pattern),
                        likeLower(cb, root.get("lastname"), pattern),
                        likeLower(cb, root.get("email"), pattern),
                        likeLower(cb, root.get("phone"), pattern),
                        cb.like(cb.lower(cb.toString(root.get("id"))), pattern),

                        likeLower(cb, genderJoin.get("name"), pattern),
                        likeLower(cb, genderJoin.get("description"), pattern),

                        likeLower(cb, addressJoin.get("street"), pattern),
                        likeLower(cb, addressJoin.get("houseNumber"), pattern),
                        likeLower(cb, addressJoin.get("zipCode"), pattern),
                        likeLower(cb, addressJoin.get("city"), pattern),
                        likeLower(cb, addressJoin.get("country"), pattern)
                )
        );
      }

      if (filter.genderId() != null) {
        if (genderJoin == null) {
          genderJoin = root.join("gender", JoinType.LEFT);
        }
        predicates.add(cb.equal(genderJoin.get("id"), filter.genderId()));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  @Nonnull
  private static Predicate likeLower(final CriteriaBuilder cb,
                                     final Expression<?> path,
                                     final String pattern) {
    return cb.like(cb.lower(cb.coalesce(path.as(String.class), Publ.EMPTY_STRING)), pattern);
  }

  @Nonnull
  private static String normalize(final String s) {
    if (s == null) {
      return Publ.EMPTY_STRING;
    }
    return s.trim().toLowerCase(Locale.ROOT);
  }
}