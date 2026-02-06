package ch.verno.ui.verno.security.api;

import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class ApiSecurityConfig {

  @Bean
  @Order(1)
  public SecurityFilterChain apiFilterChain(@Nonnull final HttpSecurity http,
                                            @Nonnull final CorsConfigurationSource apiCorsSource,
                                            @Nonnull final AuthenticationManager apiAuthenticationManager) throws Exception {
    http
            .securityMatcher("/api/**")
            .cors(cors -> cors.configurationSource(apiCorsSource))
            .csrf(AbstractHttpConfigurer::disable)
            .authenticationManager(apiAuthenticationManager)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/v1/tenants", "/api/v1/tenants/**").authenticated()
                    .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

    return http.build();
  }
}