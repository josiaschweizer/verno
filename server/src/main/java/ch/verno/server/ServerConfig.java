package ch.verno.server;

import ch.verno.server.tenant.TenantProperties;
import jakarta.annotation.Nonnull;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableConfigurationProperties(TenantProperties.class)
public class ServerConfig {

  @Bean
  @Nonnull
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}