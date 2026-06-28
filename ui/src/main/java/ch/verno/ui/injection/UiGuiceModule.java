package ch.verno.ui.injection;

import ch.verno.common.rpc.auth.InternalRpcTokenCodec;
import ch.verno.rpc.auth.InternalRpcAuthInterceptor;
import ch.verno.rpc.rpc.RpcClient;
import ch.verno.rpc.rpc.RpcFactory;
import tools.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.vaadin.flow.i18n.I18NProvider;
import jakarta.annotation.Nonnull;
import org.springframework.web.client.RestTemplate;

public class UiGuiceModule extends AbstractModule {

  @Nonnull private final I18NProvider i18NProvider;

  public UiGuiceModule(@Nonnull final I18NProvider i18NProvider) {
    this.i18NProvider = i18NProvider;
  }

  @Provides
  @Singleton
  RestTemplate restTemplate(@Nonnull final InternalRpcAuthInterceptor internalRpcAuthInterceptor) {
    final var restTemplate = new RestTemplate();
    restTemplate.getInterceptors().add(internalRpcAuthInterceptor);
    return restTemplate;
  }

  @Provides
  @Singleton
  InternalRpcAuthInterceptor internalRpcAuthInterceptor(@Nonnull final InternalRpcTokenCodec tokenCodec) {
    return new InternalRpcAuthInterceptor(tokenCodec);
  }

  @Provides
  @Singleton
  ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Provides
  @Singleton
  I18NProvider i18NProvider() {
    return i18NProvider;
  }

  @Provides
  @Singleton
  RpcClient rpcClient(@Nonnull final RestTemplate restTemplate,
                      @Nonnull final ObjectMapper objectMapper) {
    return new RpcClient("http://localhost:8081/rpc", restTemplate, objectMapper); //TODO property!!!
  }

  @Provides
  @Singleton
  RpcFactory rpcProxyFactory(RpcClient rpcClient) {
    return new RpcFactory(rpcClient);
  }
}