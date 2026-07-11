package ch.verno.server.application.properties;

import ch.verno.common.lib.application.RunMode;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConfigurationProperties(prefix = "verno.application")
public class ApplicationConfigProvider {

  @Nullable private String rpcUrl;
  @Nullable private String runMode;

  @Nullable private String apiUrl;
  @Nullable private String apiUsername;

  @Nullable
  public String getRpcUrl() {
    return rpcUrl;
  }

  public void setRpcUrl(@Nullable final String rpcUrl) {
    this.rpcUrl = rpcUrl;
  }

  @Nonnull
  public String getRunMode() {
    return Optional.ofNullable(runMode).orElse(RunMode.DEV.getKey());
  }

  public void setRunMode(@Nullable final String runMode) {
    this.runMode = runMode;
  }

  @Nullable
  public String getApiUrl() {
    return apiUrl;
  }

  public void setApiUrl(@Nullable final String apiUrl) {
    this.apiUrl = apiUrl;
  }

  @Nullable
  public String getApiUsername() {
    return apiUsername;
  }

  public void setApiUsername(@Nullable final String apiUsername) {
    this.apiUsername = apiUsername;
  }
}
