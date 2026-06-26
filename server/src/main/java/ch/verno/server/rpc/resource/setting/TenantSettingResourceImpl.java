package ch.verno.server.rpc.resource.setting;

import ch.verno.contract.dto.table.setting.TenantSettingDto;
import ch.verno.contract.endpoint.setting.TenantSettingResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.table.setting.TenantSettingBo;
import ch.verno.server.service.intern.table.setting.TenantSettingService;
import jakarta.annotation.Nonnull;

@SuppressWarnings("unused")
@RpcResource(TenantSettingResource.class)
public class TenantSettingResourceImpl implements TenantSettingResource {

  @Nonnull private final Lazy<TenantSettingBo> tenantSettingBo;
  @Nonnull private final Lazy<TenantSettingService> tenantSettingService;

  public TenantSettingResourceImpl(@Nonnull final ServerBean serverBean) {
    this.tenantSettingBo = Lazy.of(() -> serverBean.get(TenantSettingBo.class));
    this.tenantSettingService = Lazy.of(() -> serverBean.get(TenantSettingService.class));
  }

  @Nonnull
  @Override
  public TenantSettingDto getCurrentOrDefaultTenantSetting() {
    return tenantSettingBo.get().getCurrentOrDefaultTenantSetting();
  }

  @Nonnull
  @Override
  public TenantSettingDto saveTenantSetting(@Nonnull final TenantSettingDto tenantSettingDto) {
    return tenantSettingService.get().save(tenantSettingDto);
  }
}
