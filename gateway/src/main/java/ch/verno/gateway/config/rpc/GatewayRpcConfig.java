package ch.verno.gateway.config.rpc;

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

  @Nonnull
  @Bean
  public RestTemplate restTemplate() {
    final var requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setConnectTimeout(2000);
    requestFactory.setReadTimeout(10000);

    final var restTemplate = new RestTemplate(requestFactory);
    restTemplate.getInterceptors().add(new RetryingInterceptor(5, 2000));
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
                             @Nonnull final RpcProperties rpcProperties) {
    return new RpcClient(rpcProperties.getUrl(), restTemplate, objectMapper);
  }

  @Nonnull
  @Bean
  public RpcFactory rpcFactory(@Nonnull final RpcClient rpcClient) {
    return new RpcFactory(rpcClient);
  }
}