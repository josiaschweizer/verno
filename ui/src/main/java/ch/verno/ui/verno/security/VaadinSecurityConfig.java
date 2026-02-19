package ch.verno.ui.verno.security;

import ch.verno.publ.ApiUrl;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class VaadinSecurityConfig {

  @Bean
  @Order(2)
  public SecurityFilterChain internalApiFilterChain(@Nonnull HttpSecurity http) throws Exception {
    http
            .securityMatcher("/internal/api/v1/**")
            .csrf(AbstractHttpConfigurer::disable)
            .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

    return http.build();
  }

  @Bean
  @Order(3)
  public SecurityFilterChain vaadinFilterChain(@Nonnull HttpSecurity http,
                                               @Qualifier("vaadinAuthenticationManager") @Nonnull AuthenticationManager vaadinAuthenticationManager) throws Exception {
    http
            .csrf(AbstractHttpConfigurer::disable)
            .authenticationManager(vaadinAuthenticationManager)
            .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/error").permitAll()
            )
            .with(VaadinSecurityConfigurer.vaadin(), configurer ->
                    configurer.loginView(LoginView.class)
            );

    return http.build();
  }
}