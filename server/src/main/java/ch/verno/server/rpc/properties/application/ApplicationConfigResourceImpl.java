package ch.verno.server.rpc.properties.application;

import ch.verno.common.lib.application.RunMode;
import ch.verno.contract.endpoint.properties.application.ApplicationConfigResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.server.application.properties.ApplicationConfigProvider;
import ch.verno.server.bean.ServerBean;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
@RpcResource(ApplicationConfigResource.class)
public class ApplicationConfigResourceImpl implements ApplicationConfigResource {

  @Nonnull private final ApplicationConfigProvider applicationConfigProvider;

  public ApplicationConfigResourceImpl(@Nonnull final ServerBean serverBean) {
    this.applicationConfigProvider = serverBean.get(ApplicationConfigProvider.class);
  }

  @Nonnull
  @Override
  public RunMode getRunMode() {
    return RunMode.fromKey(applicationConfigProvider.getRunMode());
  }

  @Nonnull
  @Override
  public String getRpcUrl() {
    return applicationConfigProvider.getRpcUrl();
  }

  @Nonnull
  @Override
  public String getApiUrl() {
    return applicationConfigProvider.getApiUrl();
  }

  @Nonnull
  @Override
  public String getApiUsername() {
    return applicationConfigProvider.getApiUsername();
  }

}
