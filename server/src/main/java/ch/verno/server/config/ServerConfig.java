package ch.verno.server.config;

import ch.verno.server.properties.application.VernoTenantConfigProviderImpl;
import jakarta.annotation.Nonnull;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableConfigurationProperties(VernoTenantConfigProviderImpl.class)
public class ServerConfig {

  @Bean
  @Nonnull
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}