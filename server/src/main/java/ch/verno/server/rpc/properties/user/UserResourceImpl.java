package ch.verno.server.rpc.properties.user;

import ch.verno.common.exceptions.lib.UserNotAuthenticatedException;
import ch.verno.contract.dto.table.setting.AppUserSettingDto;
import ch.verno.contract.dto.table.user.AppUserDto;
import ch.verno.contract.endpoint.properties.env.EnvResource;
import ch.verno.contract.endpoint.properties.user.UserResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.lib.lib.language.Language;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.intern.table.setting.AppUserSettingService;
import ch.verno.server.service.intern.table.user.AppUserService;
import jakarta.annotation.Nonnull;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@SuppressWarnings("unused")
@RpcResource(EnvResource.class)
public class UserResourceImpl implements UserResource {

  @Nonnull private final Lazy<AppUserService> appUserService;
  @Nonnull private final Lazy<AppUserSettingService> appUserSettingService;

  public UserResourceImpl(@Nonnull final ServerBean bean) {
    this.appUserService = Lazy.of(() -> bean.get(AppUserService.class));
    this.appUserSettingService = Lazy.of(() -> bean.get(AppUserSettingService.class));
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
    if (auth == null) {
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

  @Override
  public Language getCurrentUserLanguage() {
    final var setting = getCurrentAppUserSetting();
    return setting.getLanguage();
  }

  @Override
  public boolean logout() {
    SecurityContextHolder.clearContext();
    return true;
  }
}
