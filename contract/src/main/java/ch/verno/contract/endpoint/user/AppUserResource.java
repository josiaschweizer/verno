package ch.verno.contract.endpoint.user;

import ch.verno.contract.dto.table.user.AppUserDto;
import ch.verno.contract.dto.ui.user.UserDtoUnhashedPw;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

@RpcEndpoint
public interface AppUserResource {

  @Nonnull
  Optional<AppUserDto> findByUserId(@Nonnull Long id);

  @Nonnull
  Optional<AppUserDto> findByUsername(@Nonnull String username);

  @Nonnull
  Optional<AppUserDto> findByUsernameOrEmail(@Nonnull String usernameOrEmail);

  @Nonnull
  List<AppUserDto> getAllUsers();

  void createAppUser(@Nonnull UserDtoUnhashedPw bean);

  void updateAppUser(@Nonnull UserDtoUnhashedPw bean);

}
