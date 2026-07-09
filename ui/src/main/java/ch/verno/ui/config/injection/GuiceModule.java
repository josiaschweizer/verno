package ch.verno.ui.config.injection;

import ch.verno.common.rpc.auth.internal.InternalRpcTokenCodec;
import ch.verno.rpc.auth.InternalRpcAuthInterceptor;
import ch.verno.rpc.config.RetryingInterceptor;
import ch.verno.rpc.rpc.RpcClient;
import ch.verno.rpc.rpc.RpcFactory;
import ch.verno.ui.base.shortcut.registry.ShortcutController;
import ch.verno.ui.base.shortcut.registry.ShortcutControllerImpl;
import ch.verno.ui.injection.scope.PageScope;
import ch.verno.ui.injection.scope.PageScoped;
import ch.verno.ui.injection.scope.SessionScope;
import ch.verno.ui.injection.scope.SessionScoped;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.vaadin.flow.i18n.I18NProvider;
import jakarta.annotation.Nonnull;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

public class GuiceModule extends AbstractModule {

  public static final int RETRYING_INTERCEPTOR_DELAY_MILLIS = 2000;
  public static final int RETRYING_INTERCEPTOR_MAX_ATTEMPTS = 5;
  @Nonnull private final String rpcUrl;
  @Nonnull private final I18NProvider i18NProvider;

  public GuiceModule(@Nonnull final String rpcUrl,
                     @Nonnull final I18NProvider i18NProvider) {
    this.rpcUrl = rpcUrl;
    this.i18NProvider = i18NProvider;
  }

  @Override
  protected void configure() {
    bindScope(PageScoped.class, new PageScope());
    bindScope(SessionScoped.class, new SessionScope());

    bind(ShortcutController.class).to(ShortcutControllerImpl.class);
  }

  @Provides
  @Singleton
  RestTemplate restTemplate(@Nonnull final InternalRpcAuthInterceptor internalRpcAuthInterceptor) {
    final var restTemplate = new RestTemplate();
    restTemplate.getInterceptors().add(RetryingInterceptor.simple());
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
    return new RpcClient(rpcUrl, restTemplate, objectMapper);
  }

  @Provides
  @Singleton
  RpcFactory rpcProxyFactory(@Nonnull final RpcClient rpcClient) {
    return new RpcFactory(rpcClient);
  }
}