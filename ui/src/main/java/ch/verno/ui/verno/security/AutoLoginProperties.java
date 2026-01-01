package ch.verno.ui.verno.security;

import jakarta.annotation.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "auto-login")
public class AutoLoginProperties {

  @Nullable
  private String username;

  @Nullable
  private String password;

  @Nullable
  public String getUsername() {
    return username;
  }

  public void setUsername(@Nullable final String username) {
    this.username = username;
  }

  @Nullable
  public String getPassword() {
    return password;
  }

  public void setPassword(@Nullable final String password) {
    this.password = password;
  }

  public boolean isAutoLoginEnabled() {
    return username != null && !username.isBlank()
            && password != null && !password.isBlank();
  }
}

