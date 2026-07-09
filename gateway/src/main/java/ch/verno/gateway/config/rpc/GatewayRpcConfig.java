package ch.verno.gateway.config.rpc;

import ch.verno.rpc.config.RetryingInterceptor;
import ch.verno.rpc.rpc.RpcClient;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class GatewayRpcConfig {

  public static final int CONNECT_TIMEOUT = 2000;
  public static final int READ_TIMEOUT = 10000;

  @Nonnull
  @Bean
  public RestTemplate restTemplate() {
    final var requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setConnectTimeout(CONNECT_TIMEOUT);
    requestFactory.setReadTimeout(READ_TIMEOUT);

    final var restTemplate = new RestTemplate(requestFactory);
    restTemplate.getInterceptors().add(RetryingInterceptor.simple());
    return restTemplate;
  }

  @Nonnull
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Nonnull
  @Bean
  public RpcClient rpcClient(@Nonnull final RestTemplate restTemplate,
                             @Nonnull final ObjectMapper objectMapper,
                             @Nonnull final GatewayRpcProperties gatewayRpcProperties) {
    return new RpcClient(gatewayRpcProperties.getUrl(), restTemplate, objectMapper);
  }

  @Nonnull
  @Bean
  public RpcFactory rpcFactory(@Nonnull final RpcClient rpcClient) {
    return new RpcFactory(rpcClient);
  }
}