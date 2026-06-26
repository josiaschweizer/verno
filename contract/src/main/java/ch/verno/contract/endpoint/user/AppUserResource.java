package ch.verno.contract.endpoint.user;

import ch.verno.contract.dto.filter.AppUserFilter;
import ch.verno.contract.dto.table.base.SortOrderDto;
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

  @Nonnull
  List<AppUserDto> getUsers(@Nonnull AppUserFilter filter,
                            int offset,
                            int limit,
                            List<SortOrderDto> sortOrder);

  @Nonnull
  AppUserDto saveUser(@Nonnull AppUserDto user);

  @Nonnull
  AppUserDto saveUser(@Nonnull UserDtoUnhashedPw unhashedPwUser);

  void deleteUser(@Nonnull AppUserDto dto);

}
