package ch.verno.rpc.properties.user;

import ch.verno.common.lib.Routes;
import ch.verno.contract.dto.table.setting.AppUserSettingDto;
import ch.verno.contract.dto.table.user.AppUserDto;
import ch.verno.contract.endpoint.properties.user.UserResource;
import ch.verno.lib.lib.language.Language;
import ch.verno.rpc.rpc.RpcFactory;
import ch.verno.ui.base.navigation.Navigator;
import com.vaadin.flow.component.UI;
import jakarta.annotation.Nonnull;

import java.util.Optional;
import java.util.function.Supplier;

public class UserProperties {

  @Nonnull private final UserResource userResource;

  public UserProperties(@Nonnull final RpcFactory rpcFactory) {
    this.userResource = rpcFactory.create(UserResource.class);
  }

  @Nonnull
  public AppUserDto getCurrentAppUser() {
    return userResource.getCurrentAppUser();
  }

  @Nonnull
  public Optional<AppUserDto> getOptionalCurrentAppUser() {
    return userResource.getOptionalCurrentAppUser();
  }

  @Nonnull
  public AppUserSettingDto getCurrentAppUserSetting() {
    return userResource.getCurrentAppUserSetting();
  }

  @Nonnull
  public Optional<AppUserSettingDto> getOptionalCurrentAppUserSetting() {
    return userResource.getOptionalCurrentAppUserSetting();
  }

  @Nonnull
  public AppUserSettingDto getCurrentOrEmptyAppUserSetting() {
    return userResource.getCurrentOrFallbackAppUserSetting(AppUserSettingDto::empty);
  }

  @Nonnull
  public AppUserSettingDto getCurrentOrFallbackAppUserSetting(@Nonnull Supplier<AppUserSettingDto> fallback) {
    return userResource.getCurrentOrFallbackAppUserSetting(fallback);
  }

  @Nonnull
  public Language getCurrentUserLanguage() {
    return userResource.getCurrentUserLanguage();
  }

  public void logout() {
    final var ui = UI.getCurrent();
    final var result = userResource.logout();
    if (result) {
      ui.getSession().getSession().invalidate();
      Navigator.navigateTo(Routes.LOGIN);
    }
  }

}
