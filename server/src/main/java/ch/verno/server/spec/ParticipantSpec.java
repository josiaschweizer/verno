package ch.verno.server.spec;

import ch.verno.common.db.filter.ParticipantFilter;
import ch.verno.common.util.Publ;
import ch.verno.db.entity.ParticipantEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ParticipantSpec {

  @Nonnull
  public Specification<ParticipantEntity> participantSpec(@Nonnull final ParticipantFilter filter) {
    return (root, query, cb) -> {
      final var predicates = new ArrayList<Predicate>();

      Join<?, ?> genderJoin = null;
      Join<?, ?> addressJoin = null;

      Join<?, ?> parentOneJoin = null;
      Join<?, ?> parentTwoJoin = null;
      Join<?, ?> parentOneGenderJoin = null;
      Join<?, ?> parentTwoGenderJoin = null;
      Join<?, ?> parentOneAddressJoin = null;
      Join<?, ?> parentTwoAddressJoin = null;

      Join<?, ?> coursesJoin = null;
      Join<?, ?> courseLevelsJoin = null;

      final var searchText = normalize(filter.getSearchText());
      if (!searchText.isEmpty()) {
        query.distinct(true);

        final var pattern = "%" + searchText + "%";

        genderJoin = root.join("gender", JoinType.LEFT);
        addressJoin = root.join("address", JoinType.LEFT);
        parentOneJoin = root.join("parentOne", JoinType.LEFT);
        parentTwoJoin = root.join("parentTwo", JoinType.LEFT);

        parentOneGenderJoin = parentOneJoin.join("gender", JoinType.LEFT);
        parentTwoGenderJoin = parentTwoJoin.join("gender", JoinType.LEFT);
        parentOneAddressJoin = parentOneJoin.join("address", JoinType.LEFT);
        parentTwoAddressJoin = parentTwoJoin.join("address", JoinType.LEFT);

        coursesJoin = root.join("courses", JoinType.LEFT);
        courseLevelsJoin = root.join("courseLevels", JoinType.LEFT);

        final var instructorJoin = coursesJoin.join("instructor", JoinType.LEFT);

        final List<Predicate> orPredicates = new ArrayList<>();

        orPredicates.add(likeLower(cb, root.get("firstname"), pattern));
        orPredicates.add(likeLower(cb, root.get("lastname"), pattern));
        orPredicates.add(likeLower(cb, root.get("email"), pattern));
        orPredicates.add(likeLower(cb, root.get("phone"), pattern));
        orPredicates.add(likeLower(cb, root.get("note"), pattern));
        orPredicates.add(cb.like(cb.lower(cb.toString(root.get("id"))), pattern));

        final var age = tryParseInt(searchText);
        if (age != null && age >= 0 && age <= 130) {
          final var today = LocalDate.now();
          final var maxBirthdate = today.minusYears(age);
          final var minBirthdate = today.minusYears(age + 1).plusDays(1);
          orPredicates.add(cb.between(root.get("birthdate"), minBirthdate, maxBirthdate));
        }

        orPredicates.add(likeLower(cb, genderJoin.get("name"), pattern));
        orPredicates.add(likeLower(cb, genderJoin.get("description"), pattern));

        orPredicates.add(likeLower(cb, addressJoin.get("street"), pattern));
        orPredicates.add(likeLower(cb, addressJoin.get("houseNumber"), pattern));
        orPredicates.add(likeLower(cb, addressJoin.get("zipCode"), pattern));
        orPredicates.add(likeLower(cb, addressJoin.get("city"), pattern));
        orPredicates.add(likeLower(cb, addressJoin.get("country"), pattern));

        orPredicates.add(likeLower(cb, coursesJoin.get("title"), pattern));
        orPredicates.add(likeLower(cb, coursesJoin.get("location"), pattern));
        orPredicates.add(cb.like(cb.lower(cb.toString(coursesJoin.get("capacity"))), pattern));

        orPredicates.add(likeLower(cb, instructorJoin.get("firstname"), pattern));
        orPredicates.add(likeLower(cb, instructorJoin.get("lastname"), pattern));
        orPredicates.add(likeLower(cb, instructorJoin.get("email"), pattern));
        orPredicates.add(likeLower(cb, instructorJoin.get("phone"), pattern));

        orPredicates.add(likeLower(cb, courseLevelsJoin.get("code"), pattern));
        orPredicates.add(likeLower(cb, courseLevelsJoin.get("name"), pattern));
        orPredicates.add(likeLower(cb, courseLevelsJoin.get("description"), pattern));
        orPredicates.add(cb.like(cb.lower(cb.toString(courseLevelsJoin.get("sortingOrder"))), pattern));

        orPredicates.add(likeLower(cb, parentOneJoin.get("firstname"), pattern));
        orPredicates.add(likeLower(cb, parentOneJoin.get("lastname"), pattern));
        orPredicates.add(likeLower(cb, parentOneJoin.get("email"), pattern));
        orPredicates.add(likeLower(cb, parentOneJoin.get("phone"), pattern));

        orPredicates.add(likeLower(cb, parentOneGenderJoin.get("name"), pattern));
        orPredicates.add(likeLower(cb, parentOneGenderJoin.get("description"), pattern));

        orPredicates.add(likeLower(cb, parentOneAddressJoin.get("street"), pattern));
        orPredicates.add(likeLower(cb, parentOneAddressJoin.get("houseNumber"), pattern));
        orPredicates.add(likeLower(cb, parentOneAddressJoin.get("zipCode"), pattern));
        orPredicates.add(likeLower(cb, parentOneAddressJoin.get("city"), pattern));
        orPredicates.add(likeLower(cb, parentOneAddressJoin.get("country"), pattern));

        orPredicates.add(likeLower(cb, parentTwoJoin.get("firstname"), pattern));
        orPredicates.add(likeLower(cb, parentTwoJoin.get("lastname"), pattern));
        orPredicates.add(likeLower(cb, parentTwoJoin.get("email"), pattern));
        orPredicates.add(likeLower(cb, parentTwoJoin.get("phone"), pattern));

        orPredicates.add(likeLower(cb, parentTwoGenderJoin.get("name"), pattern));
        orPredicates.add(likeLower(cb, parentTwoGenderJoin.get("description"), pattern));

        orPredicates.add(likeLower(cb, parentTwoAddressJoin.get("street"), pattern));
        orPredicates.add(likeLower(cb, parentTwoAddressJoin.get("houseNumber"), pattern));
        orPredicates.add(likeLower(cb, parentTwoAddressJoin.get("zipCode"), pattern));
        orPredicates.add(likeLower(cb, parentTwoAddressJoin.get("city"), pattern));
        orPredicates.add(likeLower(cb, parentTwoAddressJoin.get("country"), pattern));

        predicates.add(cb.or(orPredicates.toArray(new Predicate[0])));
      }

      if (filter.getGenderIds() != null && !filter.getGenderIds().isEmpty()) {
        if (genderJoin == null) {
          genderJoin = root.join("gender", JoinType.LEFT);
        }
        predicates.add(genderJoin.get("id").in(filter.getGenderIds()));
      }

      if (filter.getCourseIds() != null && !filter.getCourseIds().isEmpty()) {
        query.distinct(true);
        if (coursesJoin == null) {
          coursesJoin = root.join("courses", JoinType.LEFT);
        }
        predicates.add(coursesJoin.get("id").in(filter.getCourseIds()));
      }

      if (filter.getCourseLevelIds() != null && !filter.getCourseLevelIds().isEmpty()) {
        query.distinct(true);
        if (courseLevelsJoin == null) {
          courseLevelsJoin = root.join("courseLevels", JoinType.LEFT);
        }
        predicates.add(courseLevelsJoin.get("id").in(filter.getCourseLevelIds()));
      }

      if (filter.getBirthDateFrom() != null) {
        predicates.add(cb.greaterThanOrEqualTo(root.get("birthdate"), filter.getBirthDateFrom()));
      }

      if (filter.getBirthDateTo() != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get("birthdate"), filter.getBirthDateTo()));
      }

      if (filter.isActive() != null) {
        predicates.add(cb.equal(root.get("active"), filter.isActive()));
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
  private static Integer tryParseInt(@Nullable final String s) {
    if (s == null || s.isBlank()) {
      return null;
    }
    try {
      return Integer.parseInt(s.trim());
    } catch (NumberFormatException e) {
      return null;
    }
  }

  @Nonnull
  private static String normalize(@Nullable final String s) {
    if (s == null) {
      return Publ.EMPTY_STRING;
    }
    return s.trim().toLowerCase(Locale.ROOT);
  }
}