package ch.verno.contract.endpoint.properties.user;

import ch.verno.common.exceptions.lib.UserNotAuthenticatedException;
import ch.verno.contract.dto.table.setting.AppUserSettingDto;
import ch.verno.contract.dto.table.user.AppUserDto;
import ch.verno.contract.rpc.RpcEndpoint;
import ch.verno.lib.lib.language.Language;
import jakarta.annotation.Nonnull;

import java.util.Optional;
import java.util.function.Supplier;

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
   * Returns the settings of the currently authenticated application user, if both the user and its settings exist.
   *
   * @return an {@link Optional} containing the current user's settings, or empty if no user is authenticated
   * or no settings exist for that user
   */
  @Nonnull
  Optional<AppUserSettingDto> getOptionalCurrentAppUserSetting();

  /**
   * Returns the settings of the currently authenticated user or a fallback value.
   * <p>
   * The fallback supplier is used when no user is authenticated or when no settings exist for the
   * authenticated user.
   *
   * @param fallback supplier that creates the fallback settings
   * @return the current user's settings, or the supplied fallback settings
   */
  @Nonnull
  AppUserSettingDto getCurrentOrFallbackAppUserSetting(@Nonnull Supplier<AppUserSettingDto> fallback);

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
