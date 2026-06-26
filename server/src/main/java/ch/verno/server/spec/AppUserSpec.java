package ch.verno.server.spec;

import ch.verno.contract.dto.filter.AppUserFilter;
import ch.verno.db.entity.user.AppUserEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.domain.Specification;

public class AppUserSpec extends BaseSpec<AppUserEntity, AppUserFilter> {

  @Nonnull
  @Override
  public Specification<AppUserEntity> getSpecification(@Nonnull final AppUserFilter filter) {
    return (root, query, cb) -> {
      return cb.like(cb.lower(root.get("username")), "%" + filter.searchText().toLowerCase() + "%");
    };
  }

}
