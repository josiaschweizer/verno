package ch.verno.gateway;

import ch.verno.contract.endpoint.properties.application.ApplicationConfigResource;
import ch.verno.contract.endpoint.properties.env.EnvResource;
import ch.verno.lib.VernoSecrets;
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
    final var mockApplicationConfig = Mockito.mock(ApplicationConfigResource.class);
    Mockito.when(mockApplicationConfig.getApiUsername()).thenReturn(TEST_USER);
    Mockito.when(mockFactory.create(ApplicationConfigResource.class)).thenReturn(mockApplicationConfig);

    final var mockEnvResource = Mockito.mock(EnvResource.class);
    Mockito.when(mockEnvResource.getEnv(VernoSecrets.API_PASSWORD)).thenReturn(TEST_PASSWORD);
    Mockito.when(mockFactory.create(EnvResource.class)).thenReturn(mockEnvResource);

    return mockFactory;
  }
}