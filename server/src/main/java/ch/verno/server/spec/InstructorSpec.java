package ch.verno.server.spec;

import ch.verno.common.db.constants.address.AddressConstants;
import ch.verno.common.db.constants.gender.GenderConstants;
import ch.verno.common.db.constants.instructor.InstructorConstants;
import ch.verno.contract.dto.filter.InstructorFilter;
import ch.verno.db.entity.instructor.InstructorEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class InstructorSpec extends BaseSpec<InstructorEntity, InstructorFilter> {

  @Nonnull
  @Override
  public Specification<InstructorEntity> getSpecification(@Nonnull final InstructorFilter filter) {
    return (root, query, cb) -> {
      final var predicates = new ArrayList<Predicate>();

      Join<?, ?> addressJoin;
      Join<?, ?> genderJoin = null;

      final var searchText = normalize(filter.searchText());
      if (!searchText.isEmpty()) {
        query.distinct(true);

        final var pattern = "%" + searchText + "%";

        addressJoin = root.join(AddressConstants.ENTITY_NAME, JoinType.LEFT);
        genderJoin = root.join(GenderConstants.ENTITY_NAME, JoinType.LEFT);

        predicates.add(
                cb.or(
                        likeLower(cb, root.get(InstructorConstants.FIRSTNAME), pattern),
                        likeLower(cb, root.get(InstructorConstants.LASTNAME), pattern),
                        likeLower(cb, root.get(InstructorConstants.EMAIL), pattern),
                        likeLower(cb, root.get(InstructorConstants.PHONE), pattern),
                        cb.like(cb.lower(cb.toString(root.get(InstructorConstants.ID))), pattern),

                        likeLower(cb, genderJoin.get(GenderConstants.NAME), pattern),
                        likeLower(cb, genderJoin.get(GenderConstants.DESCRIPTION), pattern),

                        likeLower(cb, addressJoin.get(AddressConstants.STREET), pattern),
                        likeLower(cb, addressJoin.get(AddressConstants.HOUSE_NUMBER), pattern),
                        likeLower(cb, addressJoin.get(AddressConstants.ZIPCODE), pattern),
                        likeLower(cb, addressJoin.get(AddressConstants.CITY), pattern),
                        likeLower(cb, addressJoin.get(AddressConstants.COUNTRY), pattern)
                )
        );
      }

      if (filter.genderId() != null) {
        if (genderJoin == null) {
          genderJoin = root.join(GenderConstants.ENTITY_NAME, JoinType.LEFT);
        }
        predicates.add(cb.equal(genderJoin.get(GenderConstants.ID), filter.genderId()));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}