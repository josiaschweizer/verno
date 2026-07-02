package ch.verno.gateway.endpoints.v1.pub.health;

import ch.verno.common.lib.api.ApiUrl;
import ch.verno.gateway.BaseApiTest;
import ch.verno.lib.annotation.test.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class TestHealthController extends BaseApiTest {

  @Test
  @DisplayName("reach health endpoint without authentication")
  void testHealthPoint_1() throws Exception {
    performGet(ApiUrl.HEALTH)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("OK"));
  }
}