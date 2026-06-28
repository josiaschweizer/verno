package ch.verno.server.rpc.resource.user;

import ch.verno.common.exceptions.lib.UserNotAuthenticatedException;
import ch.verno.contract.dto.filter.AppUserFilter;
import ch.verno.contract.dto.response.base.delete.DeleteResponse;
import ch.verno.contract.dto.response.base.save.SaveResponse;
import ch.verno.contract.dto.table.base.SortOrderDto;
import ch.verno.contract.dto.table.setting.AppUserSettingDto;
import ch.verno.contract.dto.table.user.AppUserDto;
import ch.verno.contract.dto.ui.user.UserDtoUnhashedPw;
import ch.verno.contract.endpoint.user.AppUserResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.lib.lib.language.Language;
import ch.verno.lib.lib.language.LanguageUtil;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.table.user.AppUserBo;
import ch.verno.server.service.intern.table.setting.AppUserSettingService;
import ch.verno.server.service.intern.table.user.AppUserService;
import jakarta.annotation.Nonnull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Component
@RpcResource(AppUserResource.class)
public class AppUserResourceImpl implements AppUserResource {

  @Nonnull private final PasswordEncoder passwordEncoder;
  @Nonnull private final Lazy<AppUserBo> appUserBo;
  @Nonnull private final Lazy<AppUserService> appUserService;
  @Nonnull private final Lazy<AppUserSettingService> appUserSettingService;

  public AppUserResourceImpl(@Nonnull final ServerBean bean) {
    this.passwordEncoder = bean.get(PasswordEncoder.class);
    this.appUserBo = Lazy.of(() -> bean.get(AppUserBo.class));
    this.appUserService = Lazy.of(() -> bean.get(AppUserService.class));
    this.appUserSettingService = Lazy.of(() -> bean.get(AppUserSettingService.class));
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

  @Nonnull
  @Override
  public List<AppUserDto> getUsers(@Nonnull final AppUserFilter filter,
                                   @Nonnull final List<SortOrderDto> sortOrder,
                                   final int offset,
                                   final int limit) {
    return appUserService.get().findAll(filter, sortOrder, offset, limit);
  }

  @Nonnull
  @Override
  public AppUserDto saveUser(@Nonnull final AppUserDto user) {
    return appUserService.get().save(user);
  }

  @Nonnull
  @Override
  public AppUserDto saveUser(@Nonnull final UserDtoUnhashedPw unhashedPwUser) {
    final var hashedPw = passwordEncoder.encode(unhashedPwUser.getPassword());
    final var appUser = unhashedPwUser.toAppUserDtoUnhashedPw();
    appUser.setPasswordHash(hashedPw);
    return saveUser(appUser);
  }

  @Nonnull
  @Override
  public SaveResponse<AppUserDto> updatePassword(@Nonnull final Long userId,
                                                 @Nonnull final String newPassword) {
    return appUserBo.get().changePassword(userId, newPassword);
  }

  @Nonnull
  @Override
  public DeleteResponse deleteUser(@Nonnull final AppUserDto dto) {
    return appUserService.get().delete(dto);
  }

  @Nonnull
  @Override
  public AppUserDto getCurrentAppUser() {
    final var currentUserOptional = getOptionalCurrentAppUser();
    if (currentUserOptional.isEmpty()) {
      throw new UserNotAuthenticatedException("Authenticated user not found");
    }

    return currentUserOptional.get();
  }

  @Nonnull
  @Override
  public Optional<AppUserDto> getOptionalCurrentAppUser() {
    final var auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null ||
            !auth.isAuthenticated() ||
            auth instanceof AnonymousAuthenticationToken) {
      return Optional.empty();
    }

    return appUserService.get().findByUsername(auth.getName());
  }

  @Nonnull
  @Override
  public AppUserSettingDto getCurrentAppUserSetting() {
    final var user = getCurrentAppUser();
    final var setting = appUserSettingService.get().findByUserId(user.getId());
    if (setting.isEmpty()) {
      throw new IllegalStateException("User Setting for user " + user.getUsername() + " not found");
    }

    return setting.get();
  }

  @Nonnull
  @Override
  public Optional<AppUserSettingDto> getOptionalCurrentAppUserSetting() {
    final var user = getOptionalCurrentAppUser();
    return user.flatMap(appUserDto -> appUserSettingService.get().findByUserId(appUserDto.getId()));
  }

  @Override
  public @Nonnull Language getCurrentUserLanguage() {
    final var setting = getCurrentAppUserSetting();
    return setting.getLanguage();
  }

  @Nonnull
  @Override
  public Language getCurrentOrDefaultUserLanguage() {
    final var setting = getOptionalCurrentAppUserSetting();
    return setting.map(AppUserSettingDto::getLanguage).orElseGet(LanguageUtil::getDefaultLanguage);

  }

  @Override
  public boolean logout() {
    SecurityContextHolder.clearContext();
    return true;
  }
}