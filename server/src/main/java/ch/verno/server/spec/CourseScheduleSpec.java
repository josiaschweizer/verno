package ch.verno.server.spec;

import ch.verno.common.db.constants.course.CourseScheduleConstants;
import ch.verno.contract.dto.filter.CourseScheduleFilter;
import ch.verno.db.entity.course.CourseScheduleEntity;
import ch.verno.lib.New;
import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class CourseScheduleSpec extends BaseSpec<CourseScheduleEntity, CourseScheduleFilter> {

  @Nonnull
  @Override
  public Specification<CourseScheduleEntity> getSpecification(@Nonnull final CourseScheduleFilter filter) {
    return (root, query, cb) -> {
      final var predicates = New.<Predicate>list();

      Join<CourseScheduleEntity, String> weeksJoin = null;


      final var searchText = BaseSpec.normalize(filter.getSearchText());
      if (!searchText.isEmpty()) {
        query.distinct(true);
        final var pattern = "%" + searchText + "%";

        weeksJoin = root.join(CourseScheduleConstants.WEEKS, JoinType.LEFT);

        predicates.add(
                cb.or(
                        likeLower(cb, root.get(CourseScheduleConstants.TITLE), pattern),
                        cb.like(cb.lower(cb.toString(root.get(CourseScheduleConstants.ID))), pattern),
                        cb.like(cb.lower(cb.toString(root.get(CourseScheduleConstants.STATUS))), pattern),
                        likeLower(cb, weeksJoin, pattern)
                )
        );
      }

      if (filter.getWeek() != null) {
        query.distinct(true);
        if (weeksJoin == null) {
          weeksJoin = root.join(CourseScheduleConstants.WEEKS, JoinType.INNER);
        }

        final var weekAsString = String.valueOf(filter.getWeek());
        predicates.add(cb.equal(weeksJoin, weekAsString));
      }
      if (filter.getStatus() != null) {
        predicates.add(cb.equal(root.get(CourseScheduleConstants.STATUS), filter.getStatus()));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}