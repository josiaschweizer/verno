package ch.verno.server.application.properties;

import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConfigurationProperties(prefix = "verno.api")
public class ApiConfigProvider {

  @Nullable private String url;
  @Nullable private String username;
  @Nullable private String password; //TODO MOVE TO ENV!!!

  @Nonnull
  public String getUrl() {
    return Optional.ofNullable(url).orElse(Publ.EMPTY_STRING);
  }

  public void setUrl(@Nullable final String url) {
    this.url = url;
  }

  @Nonnull
  public String getUsername() {
    return Optional.ofNullable(username).orElse(Publ.EMPTY_STRING);
  }

  public void setUsername(@Nullable final String username) {
    this.username = username;
  }

  @Nonnull
  public String getPassword() {
    return Optional.ofNullable(password).orElse(Publ.EMPTY_STRING);
  }

  public void setPassword(@Nullable final String password) {
    this.password = password;
  }
}
