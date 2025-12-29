package ch.verno.server.spec;

import ch.verno.common.db.filter.InstructorFilter;
import ch.verno.common.util.Publ;
import ch.verno.db.entity.InstructorEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Locale;

public class InstructorSpec {

  @Nonnull
  public Specification<InstructorEntity> instructorSpec(@Nonnull final InstructorFilter filter) {
    return (root, query, cb) -> {
      final var predicates = new ArrayList<Predicate>();

      final var searchText = normalize(filter.searchText());
      if (!searchText.isEmpty()) {
        query.distinct(true);

        final var pattern = "%" + searchText + "%";
        final var firstName = cb.like(cb.lower(root.get("firstname")), pattern);
        final var lastName = cb.like(cb.lower(root.get("lastname")), pattern);
        final var email = cb.like(cb.lower(root.get("email")), pattern);
        final var phone = cb.like(cb.lower(root.get("phone")), pattern);

        final var addressJoin = root.join("address", JoinType.LEFT);
        final var city = cb.like(cb.lower(addressJoin.get("city")), pattern);
        final var street = cb.like(cb.lower(addressJoin.get("street")), pattern);

        predicates.add(cb.or(firstName, lastName, email, phone, city, street));
      }

      if (filter.genderId() != null) {
        predicates.add(cb.equal(root.get("gender").get("id"), filter.genderId()));
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