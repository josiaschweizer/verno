package ch.verno.server.rpc.properties.application;

import ch.verno.common.lib.application.RunMode;
import ch.verno.contract.endpoint.properties.application.ApplicationResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.applicationproperties.ApplicationConfigProvider;
import ch.verno.server.bean.ServerBean;
import jakarta.annotation.Nonnull;

@RpcResource(ApplicationResource.class)
public class ApplicationResourceImpl implements ApplicationResource {

  @Nonnull private final Lazy<ApplicationConfigProvider> applicationConfigProvider;

  public ApplicationResourceImpl(@Nonnull final ServerBean serverBean) {
    this.applicationConfigProvider = Lazy.of(() -> serverBean.get(ApplicationConfigProvider.class));
  }

  @Nonnull
  @Override
  public RunMode getRunMode() {
    return RunMode.fromKey(applicationConfigProvider.get().getRunMode());
  }

}
