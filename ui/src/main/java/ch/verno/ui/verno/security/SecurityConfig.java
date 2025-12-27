package ch.verno.ui.verno.security;

import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  @Nonnull
  SecurityFilterChain securityFilterChain(@Nonnull final HttpSecurity http) {
    return http.with(VaadinSecurityConfigurer.vaadin(), configurer -> {
      configurer.loginView(LoginView.class);
    }).build();
  }

  @Bean
  @Nonnull
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}