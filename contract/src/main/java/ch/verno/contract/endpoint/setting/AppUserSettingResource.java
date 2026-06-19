package ch.verno.contract.endpoint.setting;

import ch.verno.contract.dto.table.setting.AppUserSettingDto;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

import java.util.Optional;

@RpcEndpoint
public interface AppUserSettingResource {

  @Nonnull
  Optional<AppUserSettingDto> getAppUserSettingByUserId(@Nonnull Long userId);

}
