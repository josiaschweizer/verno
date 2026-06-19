package ch.verno.server.bo.table.user;

import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.intern.table.user.AppUserService;
import jakarta.annotation.Nonnull;

public class UserBo {

  @Nonnull private final Lazy<AppUserService> appUserService;

  protected UserBo(@Nonnull final ServerBean bean) {
    this.appUserService = Lazy.of(() -> bean.get(AppUserService.class));
  }

}
