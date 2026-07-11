package ch.verno.server.spec;

import ch.verno.common.db.constants.address.AddressConstants;
import ch.verno.common.db.constants.course.CourseConstants;
import ch.verno.common.db.constants.course.CourseLevelConstants;
import ch.verno.common.db.constants.gender.GenderConstants;
import ch.verno.common.db.constants.instructor.InstructorConstants;
import ch.verno.common.db.constants.participant.ParentConstants;
import ch.verno.common.db.constants.participant.ParticipantConstants;
import ch.verno.contract.dto.filter.ParticipantFilter;
import ch.verno.db.entity.participant.ParticipantEntity;
import ch.verno.lib.New;
import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ParticipantSpec extends BaseSpec<ParticipantEntity, ParticipantFilter> {

  private static final int MIN_AGE = 0;
  private static final int MAX_AGE = 130;

  @Override
  public Specification<ParticipantEntity> getSpecification(@Nonnull final ParticipantFilter filter) {
    return (root, query, cb) -> {
      final var predicates = new ArrayList<Predicate>();

      Join<?, ?> genderJoin = null;
      Join<?, ?> coursesJoin = null;
      Join<?, ?> courseLevelsJoin = null;

      final var searchText = normalize(filter.getSearchText());
      if (!searchText.isEmpty()) {
        query.distinct(true);

        final var pattern = "%" + searchText + "%";

        genderJoin = root.join(GenderConstants.ENTITY_NAME, JoinType.LEFT);
        final var addressJoin = root.join(AddressConstants.ENTITY_NAME, JoinType.LEFT);
        coursesJoin = root.join(CourseConstants.MANY_ENTITY_NAME, JoinType.LEFT);
        courseLevelsJoin = root.join(CourseLevelConstants.MANY_ENTITY_NAME, JoinType.LEFT);
        final var instructorJoin = coursesJoin.join(InstructorConstants.ENTITY_NAME, JoinType.LEFT);

        final var orPredicates = New.<Predicate>list();

        orPredicates.add(likeLower(cb, root.get(ParticipantConstants.FIRSTNAME), pattern));
        orPredicates.add(likeLower(cb, root.get(ParticipantConstants.LASTNAME), pattern));
        orPredicates.add(likeLower(cb, root.get(ParticipantConstants.EMAIL), pattern));
        orPredicates.add(likeLower(cb, root.get(ParticipantConstants.PHONE), pattern));
        orPredicates.add(likeLower(cb, root.get(ParticipantConstants.NOTE), pattern));
        orPredicates.add(cb.like(cb.lower(cb.toString(root.get(ParticipantConstants.ID))), pattern));

        final var age = tryParseInt(searchText);
        if (age != null && age >= MIN_AGE && age <= MAX_AGE) {
          final var today = LocalDate.now();
          final var maxBirthdate = today.minusYears(age);
          final var minBirthdate = today.minusYears(age + 1).plusDays(1);
          orPredicates.add(cb.between(root.get(ParticipantConstants.BIRTHDATE), minBirthdate, maxBirthdate));
        }

        addGenderAndAddress(orPredicates, cb, genderJoin, addressJoin, pattern);

        orPredicates.add(likeLower(cb, coursesJoin.get(CourseConstants.TITLE), pattern));
        orPredicates.add(likeLower(cb, coursesJoin.get(CourseConstants.LOCATION), pattern));
        orPredicates.add(cb.like(cb.lower(cb.toString(coursesJoin.get(CourseConstants.CAPACITY))), pattern));

        orPredicates.add(likeLower(cb, instructorJoin.get(InstructorConstants.FIRSTNAME), pattern));
        orPredicates.add(likeLower(cb, instructorJoin.get(InstructorConstants.LASTNAME), pattern));
        orPredicates.add(likeLower(cb, instructorJoin.get(InstructorConstants.EMAIL), pattern));
        orPredicates.add(likeLower(cb, instructorJoin.get(InstructorConstants.PHONE), pattern));

        orPredicates.add(likeLower(cb, courseLevelsJoin.get(CourseLevelConstants.CODE), pattern));
        orPredicates.add(likeLower(cb, courseLevelsJoin.get(CourseLevelConstants.NAME), pattern));
        orPredicates.add(likeLower(cb, courseLevelsJoin.get(CourseLevelConstants.DESCRIPTION), pattern));
        orPredicates.add(cb.like(cb.lower(cb.toString(courseLevelsJoin.get(CourseLevelConstants.SORTING_ORDER))), pattern));

        addParentPredicates(orPredicates, cb, root.join(ParticipantConstants.PARENT_ONE, JoinType.LEFT), pattern);
        addParentPredicates(orPredicates, cb, root.join(ParticipantConstants.PARENT_TWO, JoinType.LEFT), pattern);

        predicates.add(cb.or(orPredicates.toArray(new Predicate[0])));
      }

      if (filter.getGenderIds() != null && !filter.getGenderIds().isEmpty()) {
        if (genderJoin == null) {
          genderJoin = root.join(GenderConstants.ENTITY_NAME, JoinType.LEFT);
        }
        predicates.add(genderJoin.get(GenderConstants.ID).in(filter.getGenderIds()));
      }

      if (filter.getCourseIds() != null && !filter.getCourseIds().isEmpty()) {
        query.distinct(true);
        if (coursesJoin == null) {
          coursesJoin = root.join(CourseConstants.MANY_ENTITY_NAME, JoinType.LEFT);
        }
        predicates.add(coursesJoin.get(CourseConstants.ID).in(filter.getCourseIds()));
      }

      if (filter.getCourseLevelIds() != null && !filter.getCourseLevelIds().isEmpty()) {
        query.distinct(true);
        if (courseLevelsJoin == null) {
          courseLevelsJoin = root.join(CourseLevelConstants.MANY_ENTITY_NAME, JoinType.LEFT);
        }
        predicates.add(courseLevelsJoin.get(CourseLevelConstants.ID).in(filter.getCourseLevelIds()));
      }

      if (filter.getBirthDateFrom() != null) {
        predicates.add(cb.greaterThanOrEqualTo(root.get(ParticipantConstants.BIRTHDATE), filter.getBirthDateFrom()));
      }

      if (filter.getBirthDateTo() != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get(ParticipantConstants.BIRTHDATE), filter.getBirthDateTo()));
      }

      if (filter.isActive() != null) {
        predicates.add(cb.equal(root.get(ParticipantConstants.ACTIVE), filter.isActive()));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  private void addParentPredicates(@Nonnull final List<Predicate> orPredicates,
                                   @Nonnull final CriteriaBuilder cb,
                                   @Nonnull final Join<?, ?> parentJoin,
                                   @Nonnull final String pattern) {
    orPredicates.add(likeLower(cb, parentJoin.get(ParentConstants.FIRSTNAME), pattern));
    orPredicates.add(likeLower(cb, parentJoin.get(ParentConstants.LASTNAME), pattern));
    orPredicates.add(likeLower(cb, parentJoin.get(ParentConstants.EMAIL), pattern));
    orPredicates.add(likeLower(cb, parentJoin.get(ParentConstants.PHONE), pattern));

    final var genderJoin = parentJoin.join(GenderConstants.ENTITY_NAME, JoinType.LEFT);
    final var addressJoin = parentJoin.join(AddressConstants.ENTITY_NAME, JoinType.LEFT);
    addGenderAndAddress(orPredicates, cb, genderJoin, addressJoin, pattern);
  }

  private void addGenderAndAddress(@Nonnull final List<Predicate> orPredicates,
                                   @Nonnull final CriteriaBuilder cb,
                                   @Nonnull final Join<?, ?> genderJoin,
                                   @Nonnull final Join<?, ?> addressJoin,
                                   @Nonnull final String pattern) {
    orPredicates.add(likeLower(cb, genderJoin.get(GenderConstants.NAME), pattern));
    orPredicates.add(likeLower(cb, genderJoin.get(GenderConstants.DESCRIPTION), pattern));

    orPredicates.add(likeLower(cb, addressJoin.get(AddressConstants.STREET), pattern));
    orPredicates.add(likeLower(cb, addressJoin.get(AddressConstants.HOUSE_NUMBER), pattern));
    orPredicates.add(likeLower(cb, addressJoin.get(AddressConstants.ZIPCODE), pattern));
    orPredicates.add(likeLower(cb, addressJoin.get(AddressConstants.CITY), pattern));
    orPredicates.add(likeLower(cb, addressJoin.get(AddressConstants.COUNTRY), pattern));
  }

  @Nonnull
  @Override
  public String resolveSortProperty(@Nonnull final String property) {
    if (property.equals(ParticipantConstants.ID)) {
    }
    return super.resolveSortProperty(property);
  }
}