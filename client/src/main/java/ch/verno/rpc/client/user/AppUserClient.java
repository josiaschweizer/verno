package ch.verno.rpc.client.user;

import ch.verno.contract.dto.filter.AppUserFilter;
import ch.verno.contract.dto.response.base.delete.DeleteResponse;
import ch.verno.contract.dto.response.base.save.SaveResponse;
import ch.verno.contract.dto.table.setting.AppUserSettingDto;
import ch.verno.contract.dto.table.user.AppUserDto;
import ch.verno.contract.dto.ui.user.UserDtoUnhashedPw;
import ch.verno.contract.endpoint.user.AppUserResource;
import ch.verno.lib.Lazy;
import ch.verno.lib.lib.language.Language;
import ch.verno.rpc.client.helper.SortOrderMapper;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import com.vaadin.flow.data.provider.QuerySortOrder;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class AppUserClient {

  @Nonnull private final Lazy<AppUserResource> appUserResource;

  @Inject
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

  @Nonnull
  public List<AppUserDto> getUsers(@Nonnull final AppUserFilter filter,
                                   final int offset,
                                   final int limit,
                                   @Nonnull final List<QuerySortOrder> sortOrders) {
    final var orders = SortOrderMapper.toDto(sortOrders);
    return appUserResource.get().getUsers(filter, orders, offset, limit);
  }

  @Nonnull
  public AppUserDto saveUser(@Nonnull final AppUserDto user) {
    return appUserResource.get().saveUser(user);
  }

  @Nonnull
  public AppUserDto saveUser(@Nonnull final UserDtoUnhashedPw user) {
    return appUserResource.get().saveUser(user);
  }

  @Nonnull
  public SaveResponse<AppUserDto> changePassword(@Nonnull final Long userId,
                                                 @Nonnull final String newPassword) {
    return appUserResource.get().updatePassword(userId, newPassword);
  }

  @Nonnull
  public DeleteResponse deleteUser(@Nonnull final AppUserDto user) {
    return appUserResource.get().deleteUser(user);
  }

  public AppUserDto getCurrentAppUser() {
    return appUserResource.get().getCurrentAppUser();
  }

  @Nonnull
  public Optional<AppUserDto> getOptionalCurrentAppUser() {
    return appUserResource.get().getOptionalCurrentAppUser();
  }

  @Nonnull
  public AppUserSettingDto getCurrentAppUserSetting() {
    return appUserResource.get().getCurrentAppUserSetting();
  }

  @Nonnull
  public Optional<AppUserSettingDto> getOptionalCurrentAppUserSetting() {
    return appUserResource.get().getOptionalCurrentAppUserSetting();
  }

  @Nonnull
  public AppUserSettingDto getCurrentOrEmptyAppUserSetting() {
    return appUserResource.get().getOptionalCurrentAppUserSetting().orElseGet(AppUserSettingDto::empty);
  }

  @Nonnull
  public AppUserSettingDto getCurrentOrFallbackAppUserSetting(@Nonnull Supplier<AppUserSettingDto> fallback) {
    return appUserResource.get().getOptionalCurrentAppUserSetting().orElseGet(fallback);
  }

  @Nonnull
  public Language getCurrentUserLanguage() {
    return appUserResource.get().getCurrentUserLanguage();
  }

  @Nonnull
  public Language getCurrentOrDefaultUserLanguage() {
    return appUserResource.get().getCurrentOrDefaultUserLanguage();
  }

}
