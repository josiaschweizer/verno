package ch.verno.gateway;

import ch.verno.contract.endpoint.properties.api.ApiConfigResource;
import ch.verno.rpc.rpc.RpcFactory;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class GatewayTestConfig {

  public static final String TEST_USER = "test-user";
  public static final String TEST_PASSWORD = "test-password";

  @Bean
  @Primary
  public RpcFactory rpcFactory() {
    final var mockFactory = Mockito.mock(RpcFactory.class);
    final var mockApiConfig = Mockito.mock(ApiConfigResource.class);

    Mockito.when(mockApiConfig.getApiUsername()).thenReturn(TEST_USER);
    Mockito.when(mockApiConfig.getApiPassword()).thenReturn(TEST_PASSWORD);
    Mockito.when(mockFactory.create(ApiConfigResource.class)).thenReturn(mockApiConfig);

    return mockFactory;
  }
}