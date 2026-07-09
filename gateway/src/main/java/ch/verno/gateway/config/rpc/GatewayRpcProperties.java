package ch.verno.gateway.config.rpc;

import jakarta.annotation.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "verno.rpc")
public class GatewayRpcProperties {

  @Nullable private String url;

  @Nullable
  public String getUrl() {
    return url;
  }

  public void setUrl(@Nullable final String url) {
    this.url = url;
  }
}
