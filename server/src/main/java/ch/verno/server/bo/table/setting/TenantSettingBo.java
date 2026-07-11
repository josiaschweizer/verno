package ch.verno.server.bo.table.setting;

import ch.verno.contract.dto.table.setting.TenantSettingDto;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.table.tenant.TenantBo;
import ch.verno.server.service.entity.setting.TenantSettingService;
import jakarta.annotation.Nonnull;

public class TenantSettingBo {

  @Nonnull private final Lazy<TenantBo> tenantBo;
  @Nonnull private final Lazy<TenantSettingService> tenantSettingService;

  protected TenantSettingBo(@Nonnull final ServerBean serverBean) {
    this.tenantBo = Lazy.of(() -> BoFactory.getInstance(serverBean).get(TenantBo.class));
    this.tenantSettingService = Lazy.of(() -> serverBean.get(TenantSettingService.class));
  }

  @Nonnull
  public TenantSettingDto getCurrentOrDefaultTenantSetting() {
    final var currentTenant = tenantBo.get().getCurrentTenant();
    return tenantSettingService.get().findByTenantId(currentTenant.id()).orElseGet(TenantSettingDto::empty);
  }


}
