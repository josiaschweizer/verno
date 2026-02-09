package ch.verno.server.spec;

import ch.verno.common.db.filter.AppUserFilter;
import ch.verno.db.entity.user.AppUserEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.domain.Specification;

public class AppUserSpec {

  @Nonnull
  public Specification<AppUserEntity> appUserSpec(@Nonnull final AppUserFilter filter) {
    return (root, query, cb) -> {
      return cb.like(cb.lower(root.get("username")), "%" + filter.getSearchText().toLowerCase() + "%");
    };
  }

}
