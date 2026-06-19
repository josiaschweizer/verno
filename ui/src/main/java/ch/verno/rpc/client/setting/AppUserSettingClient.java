package ch.verno.rpc.client.setting;

import ch.verno.contract.dto.table.setting.AppUserSettingDto;
import ch.verno.contract.endpoint.setting.AppUserSettingResource;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class AppUserSettingClient {

  @Nonnull private final AppUserSettingResource appUserSettingResource;

  public AppUserSettingClient(@Nonnull final RpcFactory rpcFactory) {
    this.appUserSettingResource = rpcFactory.create(AppUserSettingResource.class);
  }

  @Nonnull
  public Optional<AppUserSettingDto> getAppUserSettingByUserId(@Nonnull final Long userId) {
    return appUserSettingResource.getAppUserSettingByUserId(userId);
  }

}
