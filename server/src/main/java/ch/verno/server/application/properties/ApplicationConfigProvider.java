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

  @Nullable private String runMode;

  @Nonnull
  public String getRunMode() {
    return Optional.ofNullable(runMode).orElse(RunMode.DEV.getKey());
  }

  public void setRunMode(@Nullable final String runMode) {
    this.runMode = runMode;
  }
}
