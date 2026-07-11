package ch.verno.gateway.config.rpc;

import ch.verno.common.rpc.auth.internal.InternalRpcTokenCodec;
import ch.verno.lib.properties.ApplicationPropertiesConstants;
import ch.verno.rpc.auth.InternalRpcAuthInterceptor;
import ch.verno.rpc.config.RetryingInterceptor;
import ch.verno.rpc.rpc.RpcClient;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class GatewayRpcConfig {

  public static final int CONNECT_TIMEOUT = 2000;
  public static final int READ_TIMEOUT = 10000;

  @Bean
  @Nonnull
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  @Nonnull
  public RpcFactory rpcFactory(@Nonnull final RpcClient rpcClient) {
    return new RpcFactory(rpcClient);
  }

  @Bean
  @Nonnull
  public RestTemplate restTemplate(@Nonnull final InternalRpcTokenCodec tokenCodec) {
    final var requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setConnectTimeout(CONNECT_TIMEOUT);
    requestFactory.setReadTimeout(READ_TIMEOUT);

    final var restTemplate = new RestTemplate(requestFactory);
    restTemplate.getInterceptors().add(new InternalRpcAuthInterceptor(tokenCodec));
    restTemplate.getInterceptors().add(RetryingInterceptor.simple());
    return restTemplate;
  }

  @Bean
  @Nonnull
  public RpcClient rpcClient(@Nonnull final RestTemplate restTemplate,
                             @Nonnull final ObjectMapper objectMapper,
                             @Nonnull final GatewayRpcProperties gatewayRpcProperties) {
    return new RpcClient(gatewayRpcProperties.getUrl(), restTemplate, objectMapper);
  }

  @Bean
  public InternalRpcTokenCodec internalRpcTokenCodec(@Value(ApplicationPropertiesConstants.VERNO_RPC_SECRET) @Nonnull final String rpcInternalSecret) {
    return new InternalRpcTokenCodec(rpcInternalSecret);
  }
}