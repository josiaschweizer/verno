package ch.verno.gateway.config.rpc;

import ch.verno.rpc.rpc.RpcClient;
import ch.verno.rpc.rpc.RpcFactory;
import tools.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GatewayRpcConfig {

  @Nonnull
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
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
                             @Value("${verno.rpc.url}") @Nonnull final String rpcUrl) { //TODO PROPERTY!!!!!!!!!!
    return new RpcClient(rpcUrl, restTemplate, objectMapper);
  }

  @Nonnull
  @Bean
  public RpcFactory rpcFactory(@Nonnull final RpcClient rpcClient) {
    return new RpcFactory(rpcClient);
  }
}