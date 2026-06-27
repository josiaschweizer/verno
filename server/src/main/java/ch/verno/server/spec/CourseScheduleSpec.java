package ch.verno.server.spec;

import ch.verno.contract.dto.filter.CourseScheduleFilter;
import ch.verno.db.entity.course.CourseScheduleEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class CourseScheduleSpec extends BaseSpec<CourseScheduleEntity, CourseScheduleFilter> {

  @Nonnull
  @Override
  public Specification<CourseScheduleEntity> getSpecification(@Nonnull final CourseScheduleFilter filter) {
    return (root, query, cb) -> {
      final var predicates = new ArrayList<Predicate>();

      Join<CourseScheduleEntity, String> weeksJoin = null;


      final var searchText = BaseSpec.normalize(filter.getSearchText());
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

      if (filter.getWeek() != null) {
        query.distinct(true);
        if (weeksJoin == null) {
          weeksJoin = root.join("weeks", JoinType.INNER);
        }

        final var weekAsString = String.valueOf(filter.getWeek());
        predicates.add(cb.equal(weeksJoin, weekAsString));
      }
      if (filter.getStatus() != null) {
        predicates.add(cb.equal(root.get("status"), filter.getStatus()));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}