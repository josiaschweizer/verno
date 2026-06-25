package ch.verno.server.rpc.resource.user;

import ch.verno.contract.dto.table.user.AppUserDto;
import ch.verno.contract.dto.ui.user.UserDtoUnhashedPw;
import ch.verno.contract.endpoint.user.AppUserResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.intern.table.user.AppUserService;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@RpcResource(AppUserResource.class)
public class AppUserResourceImpl implements AppUserResource {

  @Nonnull private final Lazy<AppUserService> appUserService;

  public AppUserResourceImpl(@Nonnull final ServerBean serverBean) {
    this.appUserService = Lazy.of(() -> serverBean.get(AppUserService.class));
  }

  @Nonnull
  @Override
  public Optional<AppUserDto> findByUserId(@Nonnull final Long id) {
    return appUserService.get().findById(id);
  }

  @Nonnull
  @Override
  public Optional<AppUserDto> findByUsername(@Nonnull final String username) {
    return appUserService.get().findByUsername(username);
  }

  @Nonnull
  @Override
  public Optional<AppUserDto> findByUsernameOrEmail(@Nonnull final String usernameOrEmail) {
    return appUserService.get().findByUsernameOrEmail(usernameOrEmail);
  }

  @Nonnull
  @Override
  public List<AppUserDto> getAllUsers() {
    return appUserService.get().findAll();
  }

  @Override
  public void createAppUser(@Nonnull final UserDtoUnhashedPw bean) {

  }

  @Override
  public void updateAppUser(@Nonnull final UserDtoUnhashedPw bean) {

  }
}
