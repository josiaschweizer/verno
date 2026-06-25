package ch.verno.rpc.client.setting;

import ch.verno.contract.dto.table.setting.TenantSettingDto;
import ch.verno.contract.endpoint.setting.TenantSettingResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import jakarta.annotation.Nonnull;

public class TenantSettingClient {

  @Nonnull private final Lazy<TenantSettingResource> tenantSettingResource;

  @Inject
  public TenantSettingClient(@Nonnull final RpcFactory rpcFactory){
    this.tenantSettingResource = Lazy.of(() -> rpcFactory.create(TenantSettingResource.class));
  }

  @Nonnull
  public TenantSettingDto getCurrentOrDefaultTenantSetting() {
    return tenantSettingResource.get().getCurrentOrDefaultTenantSetting();
  }

  @Nonnull
  @SuppressWarnings("UnusedReturnValue")
  public TenantSettingDto saveTenantSetting(@Nonnull final  TenantSettingDto tenantSetting) {
    return tenantSettingResource.get().saveTenantSetting(tenantSetting);
  }

}
