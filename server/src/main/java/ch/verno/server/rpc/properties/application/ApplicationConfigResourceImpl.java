package ch.verno.server.rpc.properties.application;

import ch.verno.common.lib.application.RunMode;
import ch.verno.contract.endpoint.properties.application.ApplicationConfigResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.application.properties.ApplicationConfigProvider;
import ch.verno.server.application.properties.RpcConfigProvider;
import ch.verno.server.bean.ServerBean;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
@RpcResource(ApplicationConfigResource.class)
public class ApplicationConfigResourceImpl implements ApplicationConfigResource {

  @Nonnull private final Lazy<RpcConfigProvider> rpcConfigProvider;
  @Nonnull private final Lazy<ApplicationConfigProvider> applicationConfigProvider;

  public ApplicationConfigResourceImpl(@Nonnull final ServerBean serverBean) {
    this.rpcConfigProvider = Lazy.of(() -> serverBean.get(RpcConfigProvider.class));
    this.applicationConfigProvider = Lazy.of(() -> serverBean.get(ApplicationConfigProvider.class));
  }

  @Nonnull
  @Override
  public RunMode getRunMode() {
    return RunMode.fromKey(applicationConfigProvider.get().getRunMode());
  }

  @Nonnull
  @Override
  public String rpcUrl() {
    return rpcConfigProvider.get().getUrl();
  }
}
