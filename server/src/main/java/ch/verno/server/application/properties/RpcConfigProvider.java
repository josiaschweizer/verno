package ch.verno.server.application.properties;

import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConfigurationProperties(prefix = "verno.rpc")
public class RpcConfigProvider {

  @Nullable private String url;
  @Nullable private String internalSecret;

  @Nonnull
  public String getUrl() {
    return Optional.ofNullable(url).orElse(Publ.EMPTY_STRING);
  }

  public void setUrl(@Nullable final String url) {
    this.url = url;
  }

  @Nonnull
  public String getInternalSecret() {
    return Optional.ofNullable(internalSecret).orElse(Publ.EMPTY_STRING);
  }

  public void setInternalSecret(@Nullable final String internalSecret) {
    this.internalSecret = internalSecret;
  }
}
