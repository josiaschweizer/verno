package ch.verno.server.spec;

import ch.verno.contract.dto.table.base.SortOrderDto;
import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Locale;

public abstract class BaseSpec<ENTITY, FILTER> {

  public abstract Specification<ENTITY> getSpecification(@Nonnull final FILTER filter);

  @Nonnull
  public List<SortOrderDto> resolveSortOrders(@Nonnull final List<SortOrderDto> sortOrders) {
    return sortOrders.stream().map(order -> new SortOrderDto(
            resolveSortProperty(order.property()),
            order.ascending()
    )).toList();
  }

  /**
   * Returns the sort property - could potentially be overridden
   * to return custom callback / foreign relation mapping
   *
   */
  @Nonnull
  public String resolveSortProperty(@Nonnull final String property) {
    return property;
  }

  @Nonnull
  public static String createForeignRelation(@Nonnull final String foreignEntity,
                                             @Nonnull final String foreignProperty) {
    return foreignEntity + Publ.DOT + foreignProperty;
  }

  @Nonnull
  protected static Predicate likeLower(@Nonnull final CriteriaBuilder cb,
                                       @Nonnull final Expression<?> path,
                                       @Nonnull final String pattern) {
    return cb.like(cb.lower(cb.coalesce(path.as(String.class), Publ.EMPTY_STRING)), pattern);
  }

  @Nullable
  protected static Integer tryParseInt(@Nullable final String s) {
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
  protected static String normalize(@Nullable final String s) {
    if (s == null) {
      return Publ.EMPTY_STRING;
    }
    return s.trim().toLowerCase(Locale.ROOT);
  }
}
