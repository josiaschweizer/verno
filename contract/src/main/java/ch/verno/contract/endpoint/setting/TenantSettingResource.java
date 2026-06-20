package ch.verno.contract.endpoint.setting;

import ch.verno.contract.dto.table.setting.TenantSettingDto;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

@RpcEndpoint
public interface TenantSettingResource {

  @Nonnull
  TenantSettingDto getCurrentOrDefaultTenantSetting();

  @Nonnull
  TenantSettingDto saveTenantSetting(@Nonnull TenantSettingDto tenantSettingDto);

}
