package ch.verno.rpc.client.user;

import ch.verno.contract.dto.table.user.AppUserDto;
import ch.verno.contract.dto.ui.user.UserDtoUnhashedPw;
import ch.verno.contract.endpoint.user.AppUserResource;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public class AppUserClient {

  @Nonnull private final AppUserResource appUserResource;

  public AppUserClient(@Nonnull final RpcFactory rpcFactory) {
    this.appUserResource = rpcFactory.create(AppUserResource.class);
  }

  @Nonnull
  public Optional<AppUserDto> findByUsername(@Nonnull final String username) {
    return appUserResource.findByUsername(username);
  }

  @Nonnull
  public List<AppUserDto> getAllUsers() {
    return appUserResource.getAllUsers();
  }

  public void createAppUser(@Nonnull final UserDtoUnhashedPw bean) {
    appUserResource.createAppUser(bean);
  }

  public void updateAppUser(@Nonnull final UserDtoUnhashedPw bean) {
    appUserResource.updateAppUser(bean);
  }

}
