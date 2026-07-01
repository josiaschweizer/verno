package ch.verno.server.rpc.properties.api;

import ch.verno.contract.endpoint.properties.api.ApiConfigResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.application.properties.ApiConfigProvider;
import ch.verno.server.bean.ServerBean;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
@RpcResource(ApiConfigResource.class)
public class ApiConfigResourceImpl implements ApiConfigResource {

  @Nonnull private final Lazy<ApiConfigProvider> apiConfigProvider;

  public ApiConfigResourceImpl(@Nonnull final ServerBean serverBean) {
    this.apiConfigProvider = Lazy.of(() -> serverBean.get(ApiConfigProvider.class));
  }

  @Nonnull
  @Override
  public String getBaseUrl() {
    return apiConfigProvider.get().getUrl();
  }

  @Nonnull
  @Override
  public String getApiUsername() {
    return apiConfigProvider.get().getUsername();
  }

  @Nonnull
  @Override
  public String getApiPassword() {
    return apiConfigProvider.get().getPassword();
  }
}
