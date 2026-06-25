package ch.verno.rpc.client.user;

import ch.verno.contract.dto.table.user.AppUserDto;
import ch.verno.contract.dto.ui.user.UserDtoUnhashedPw;
import ch.verno.contract.endpoint.user.AppUserResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public class AppUserClient {

  @Nonnull private final Lazy<AppUserResource> appUserResource;

  public AppUserClient(@Nonnull final RpcFactory rpcFactory) {
    this.appUserResource = Lazy.of(() -> rpcFactory.create(AppUserResource.class));
  }

  @Nonnull
  public Optional<AppUserDto> findByUserId(@Nonnull final Long userId){
    return appUserResource.get().findByUserId(userId);
  }

  @Nonnull
  public Optional<AppUserDto> findByUsername(@Nonnull final String username) {
    return appUserResource.get().findByUsername(username);
  }

  @Nonnull
  public Optional<AppUserDto> findByUsernameOrEmail(@Nonnull final String username) {
    return appUserResource.get().findByUsernameOrEmail(username);
  }

  @Nonnull
  public List<AppUserDto> getAllUsers() {
    return appUserResource.get().getAllUsers();
  }

  public void createAppUser(@Nonnull final UserDtoUnhashedPw bean) {
    appUserResource.get().createAppUser(bean);
  }

  public void updateAppUser(@Nonnull final UserDtoUnhashedPw bean) {
    appUserResource.get().updateAppUser(bean);
  }

  @Nonnull
  public UserDetails loadUserByUsername(@Nonnull final String username) {
    final var userOptional = appUserResource.get().findByUsernameOrEmail(username);
    if (userOptional.isEmpty()) {
      throw new UsernameNotFoundException(username);
    }

    final var user = userOptional.get(); //TODO MAYBE REFACTOR?
    return User.withUsername(user.getUsername())
            .password(user.getPasswordHash())
            .roles(user.getRole().getRole())
            .build();
  }

}
