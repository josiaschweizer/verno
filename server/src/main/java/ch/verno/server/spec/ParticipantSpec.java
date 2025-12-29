package ch.verno.server.spec;

import ch.verno.common.db.filter.ParticipantFilter;
import ch.verno.common.util.Publ;
import ch.verno.db.entity.ParticipantEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Locale;

public class ParticipantSpec {

  @Nonnull
  public Specification<ParticipantEntity> participantSpec(@Nonnull final ParticipantFilter filter) {
    return (root, query, cb) -> {
      final var predicates = new ArrayList<Predicate>();

      final var searchText = normalize(filter.searchText());
      if (!searchText.isEmpty()) {
        query.distinct(true);

        final var pattern = "%" + searchText + "%";
        final var raw = filter.searchText() == null ? Publ.EMPTY_STRING : filter.searchText().trim();
        final var rawPattern = "%" + raw + "%";

        final var firstName = cb.like(cb.lower(root.get("firstname")), pattern);
        final var lastName = cb.like(cb.lower(root.get("lastname")), pattern);
        final var email = cb.like(cb.lower(root.get("email")), pattern);
        final var phone = cb.like(cb.lower(root.get("phone")), pattern);
        final var note = cb.like(root.get("note"), rawPattern);

        final var genderJoin = root.join("gender", JoinType.LEFT);
        final var courseJoin = root.join("course", JoinType.LEFT);
        final var levelJoin = root.join("courseLevel", JoinType.LEFT);

        final var genderName = cb.like(cb.lower(genderJoin.get("name")), pattern);
        final var courseTitle = cb.like(cb.lower(courseJoin.get("title")), pattern);
        final var levelName = cb.like(cb.lower(levelJoin.get("name")), pattern);
        final var levelCode = cb.like(cb.lower(levelJoin.get("code")), pattern);

        var instructorPredicate = cb.conjunction();
        try {
          final var instructorJoin = courseJoin.join("instructor", JoinType.LEFT);
          final var instrFirst = cb.like(cb.lower(instructorJoin.get("firstname")), pattern);
          final var instrLast = cb.like(cb.lower(instructorJoin.get("lastname")), pattern);
          instructorPredicate = cb.or(instrFirst, instrLast);
        } catch (IllegalArgumentException ignored) {
          // illegal argument exception gets ignored rn
        }

        predicates.add(
                cb.or(
                        firstName,
                        lastName,
                        email,
                        phone,
                        note,
                        genderName,
                        courseTitle,
                        levelName,
                        levelCode,
                        instructorPredicate
                )
        );
      }

      if (filter.genderId() != null) {
        predicates.add(cb.equal(root.get("gender").get("id"), filter.genderId()));
      }

      if (filter.courseId() != null) {
        predicates.add(cb.equal(root.get("course").get("id"), filter.courseId()));
      }

      if (filter.courseLevelId() != null) {
        predicates.add(cb.equal(root.get("courseLevel").get("id"), filter.courseLevelId()));
      }

      if (filter.birthDateFrom() != null) {
        predicates.add(cb.greaterThanOrEqualTo(root.get("birthdate"), filter.birthDateFrom()));
      }

      if (filter.birthDateTo() != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get("birthdate"), filter.birthDateTo()));
      }

      return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
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
