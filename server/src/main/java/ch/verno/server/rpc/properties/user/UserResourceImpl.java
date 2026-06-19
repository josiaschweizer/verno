package ch.verno.server.rpc.properties.user;

import ch.verno.contract.dto.table.user.AppUserDto;
import ch.verno.contract.endpoint.properties.env.EnvResource;
import ch.verno.contract.endpoint.properties.user.UserResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.intern.table.user.AppUserService;
import jakarta.annotation.Nonnull;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@SuppressWarnings("unused")
@RpcResource(EnvResource.class)
public class UserResourceImpl implements UserResource {

  @Nonnull private final Lazy<AppUserService> appUserService;

  public UserResourceImpl(@Nonnull final ServerBean bean) {
    this.appUserService = Lazy.of(() -> bean.get(AppUserService.class));
  }


  @Nonnull
  @Override
  public AppUserDto getCurrentUser() {
    final var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      throw new IllegalStateException("No user is authenticated");
    }

    final var found = appUserService.get().findByUsername(authentication.getName());
    if (found.isEmpty()) {
      throw new IllegalStateException("Authenticated user not found in database: " + authentication.getName());
    }

    return found.get();
  }

  @Nonnull
  @Override
  public Optional<AppUserDto> getOptionalCurrentUser() {
    final var auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null) {
      return Optional.empty();
    }

    return appUserService.get().findByUsername(auth.getName());
  }

  @Override
  public boolean logout() {
    SecurityContextHolder.clearContext();
    return true;
  }
}
