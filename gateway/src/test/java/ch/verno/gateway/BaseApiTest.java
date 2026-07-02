package ch.verno.gateway;

import ch.verno.gateway.config.rpc.GatewayRpcConfig;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Base class for gateway API tests. Boots the real gateway Spring context
 * (real security chains, real filters) with {@link GatewayTestConfig} supplying
 * a mocked {@code RpcFactory} so the context starts without a live server process.
 */
@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
@Import(GatewayTestConfig.class)
public abstract class BaseApiTest {

  @Nonnull
  protected static final String TEST_API_USERNAME = "test-user";
  @Nonnull
  protected static final String TEST_API_PASSWORD = "test-password";

  @Autowired
  private WebApplicationContext webApplicationContext;

  protected MockMvc mockMvc;

  @BeforeEach
  void setUpMockMvc() {
    mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
  }

  @Nonnull
  protected ResultActions performGet(@Nonnull final String path) throws Exception {
    return mockMvc.perform(MockMvcRequestBuilders.get(path));
  }

  @Nonnull
  protected ResultActions performGetWithBasicAuth(@Nonnull final String path) throws Exception {
    return mockMvc.perform(
            MockMvcRequestBuilders.get(path)
                    .with(SecurityMockMvcRequestPostProcessors.httpBasic(TEST_API_USERNAME, TEST_API_PASSWORD)));
  }
}