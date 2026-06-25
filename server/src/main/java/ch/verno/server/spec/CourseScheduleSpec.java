package ch.verno.server.spec;

import ch.verno.contract.dto.filter.CourseScheduleFilter;
import ch.verno.db.entity.course.CourseScheduleEntity;
import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class CourseScheduleSpec {

  @Nonnull
  public Specification<CourseScheduleEntity> courseScheduleSpec(@Nonnull final CourseScheduleFilter filter) {
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

  @Nonnull
  private static Predicate likeLower(@Nonnull final CriteriaBuilder cb,
                                     @Nonnull final Expression<?> path,
                                     @Nonnull final String pattern) {
    return cb.like(cb.lower(cb.coalesce(path.as(String.class), Publ.EMPTY_STRING)), pattern);
  }
}