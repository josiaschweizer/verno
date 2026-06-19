package ch.verno.contract.endpoint.properties.user;

import ch.verno.contract.dto.table.user.AppUserDto;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

import java.util.Optional;

@RpcEndpoint
public interface UserResource {

  /**
   *
   * @return the current user, or throws an exception if no user is authenticated
   * @throws IllegalStateException if no user is authenticated
   */
  @Nonnull
  AppUserDto getCurrentUser();

  /**
   *
   * @return the current user, or empty if no user is authenticated
   */
  @Nonnull
  Optional<AppUserDto> getOptionalCurrentUser();

  /**
   * logout current user on backend (SecurityContextHolder)
   */
  boolean logout();

}
