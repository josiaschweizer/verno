package ch.verno.contract.endpoint.user;

import ch.verno.common.exceptions.lib.UserNotAuthenticatedException;
import ch.verno.contract.dto.filter.AppUserFilter;
import ch.verno.contract.dto.response.base.delete.DeleteResponse;
import ch.verno.contract.dto.response.base.save.SaveResponse;
import ch.verno.contract.dto.table.base.SortOrderDto;
import ch.verno.contract.dto.table.setting.AppUserSettingDto;
import ch.verno.contract.dto.table.user.AppUserDto;
import ch.verno.contract.dto.ui.user.UserDtoUnhashedPw;
import ch.verno.contract.rpc.RpcEndpoint;
import ch.verno.lib.lib.language.Language;
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
                            List<SortOrderDto> sortOrder, int offset,
                            int limit);

  @Nonnull
  AppUserDto saveUser(@Nonnull AppUserDto user);

  @Nonnull
  AppUserDto saveUser(@Nonnull UserDtoUnhashedPw unhashedPwUser);

  /**
   * Updates the password of the user with the given userId
   *
   * @param userId      the id of the user which gets updated
   * @param newRawPassword the new UN-ENCODED RAW password of the user
   * @return SaveResponse of AppUserDto indicating whether the save was successful or not
   */
  @Nonnull
  SaveResponse<AppUserDto> updateRawPassword(@Nonnull Long userId,
                                             @Nonnull String newRawPassword);

  /**
   * Deletes the provided app user
   *
   * @param dto the app user dto that gets deleted
   * @return a DeleteResponse indicating whether the delete was successful or not
   */
  @Nonnull
  DeleteResponse deleteUser(@Nonnull AppUserDto dto);

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
   *
   * @return the user language of the current user
   * @throws UserNotAuthenticatedException if no user is authenticated
   * @throws IllegalStateException         if the user setting cannot be found for the current user
   */
  @Nonnull
  Language getCurrentUserLanguage();

  /**
   *
   * @return the user language or the fallback langauge from {@code LanguageUtil.getDefaultLanguage}
   */
  @Nonnull
  Language getCurrentOrDefaultUserLanguage();

  /**
   * logout current user on backend (SecurityContextHolder)
   */
  boolean logout();

}