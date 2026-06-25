package ch.verno.server.spec;

import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public abstract class BaseSpec<ENTITY, FILTER> {

  public abstract Specification<ENTITY> getSpecification(@Nonnull final FILTER filter);

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
