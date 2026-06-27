package ch.verno.server.rpc.resource.setting;

import ch.verno.contract.dto.table.setting.AppUserSettingDto;
import ch.verno.contract.endpoint.setting.AppUserSettingResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.rpc.properties.user.UserResourceImpl;
import ch.verno.server.rpc.resource.user.AppUserResourceImpl;
import ch.verno.server.service.intern.table.setting.AppUserSettingService;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RpcResource(AppUserSettingResource.class)
public class AppUserSettingResourceImpl implements AppUserSettingResource {

  @Nonnull private final Lazy<AppUserSettingService> appUserSettingService;

  public AppUserSettingResourceImpl(@Nonnull final ServerBean serverBean) {
    this.appUserSettingService = Lazy.of(() -> serverBean.get(AppUserSettingService.class));
  }

  @Nonnull
  @Override
  public Optional<AppUserSettingDto> getAppUserSettingByUserId(@Nonnull final Long userId) {
    return appUserSettingService.get().findByUserId(userId);
  }

  @Nonnull
  @Override
  public AppUserSettingDto saveAppUserSetting(@Nonnull final AppUserSettingDto appUserSettingDto) {
    return appUserSettingService.get().save(appUserSettingDto);
  }
}
