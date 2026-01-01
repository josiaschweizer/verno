package ch.verno.ui.verno.security;

import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("dev")
@EnableWebSecurity
public class DevSecurityConfig {

  @Bean
  SecurityFilterChain devSecurityFilterChain(@Nonnull final HttpSecurity http) {
    return http.with(VaadinSecurityConfigurer.vaadin(), configurer -> {
      configurer.loginView(LoginView.class);
    }).build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(@Nonnull final AuthenticationConfiguration authConfig) {
    return authConfig.getAuthenticationManager();
  }
}