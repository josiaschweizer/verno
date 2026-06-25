package ch.verno.contract.endpoint.properties.user;

import ch.verno.common.exceptions.lib.UserNotAuthenticatedException;
import ch.verno.contract.dto.table.setting.AppUserSettingDto;
import ch.verno.contract.dto.table.user.AppUserDto;
import ch.verno.contract.rpc.RpcEndpoint;
import ch.verno.lib.lib.language.Language;
import jakarta.annotation.Nonnull;

import java.util.Optional;

@RpcEndpoint
public interface UserResource {

  /**
   *
   * @return the current app user, or throws an exception if no user is authenticated
   * @throws UserNotAuthenticatedException if no user is authenticated
   */
  @Nonnull
  AppUserDto getCurrentAppUser();

  /**
   *
   * @return the current app user, or empty if no user is authenticated
   */
  @Nonnull
  Optional<AppUserDto> getOptionalCurrentAppUser();

  /**
   *
   * @return the app user setting of the user currently logged in
   * @throws UserNotAuthenticatedException if the user is not authenticated
   * @throws IllegalStateException         if the user setting cannot be found for the current user
   */
  @Nonnull
  AppUserSettingDto getCurrentAppUserSetting();

  /**
   *
   * @return the user language of the current user
   * @throws UserNotAuthenticatedException if no user is authenticated
   * @throws IllegalStateException         if the user setting cannot be found for the current user
   */
  Language getCurrentUserLanguage();

  /**
   * logout current user on backend (SecurityContextHolder)
   */
  boolean logout();

}
