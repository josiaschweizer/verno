package ch.verno.rpc.properties.application;

import ch.verno.common.lib.application.RunMode;
import ch.verno.contract.endpoint.properties.application.ApplicationConfigResource;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import jakarta.annotation.Nonnull;

public class ApplicationProperties {

  @Nonnull private final ApplicationConfigResource applicationResource;

  @Inject
  public ApplicationProperties(@Nonnull final RpcFactory rpcFactory) {
    this.applicationResource = rpcFactory.create(ApplicationConfigResource.class);
  }

  @Nonnull
  public RunMode getRunMode() {
    return applicationResource.getRunMode();
  }

  @Nonnull
  public String getRpcUrl() {
    return applicationResource.getRpcUrl();
  }

  public String getApiUrl(){
    return applicationResource.getApiUrl();
  }

  @Nonnull
  public String getApiUsername() {
    return applicationResource.getApiUsername();
  }

}
