package ch.verno.ui.verno.security;

import ch.verno.rpc.client.user.AppUserClient;
import com.google.inject.Injector;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBridgeConfig {

  @Nonnull
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Nonnull
  @Bean("appUserService")
  public UserDetailsService appUserDetailsService(@Nonnull final Injector injector) {
    final var appUserClient = injector.getInstance(AppUserClient.class);
    return appUserClient::loadUserByUsername;
  }
}