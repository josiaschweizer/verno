package ch.verno.provisioner.security;

import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(@Nonnull final HttpSecurity http) throws Exception {
    http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/actuator/health", "/actuator/info").permitAll()

                    .requestMatchers(HttpMethod.GET,  "/api/v1/tenants", "/api/v1/tenants/").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v1/tenants", "/api/v1/tenants/").permitAll()
                    .requestMatchers(HttpMethod.GET,  "/api/v1/tenants/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v1/tenants/**").permitAll()

                    .requestMatchers("/error").permitAll()
                    .requestMatchers("/api/**").authenticated()

                    .anyRequest().denyAll()
            )
            .httpBasic(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  @Nonnull
  public UserDetailsService userDetailsService(final PasswordEncoder encoder) {
    final var user = readEnvOrDefault("API_USER", "verno");
    final var pass = readEnvOrDefault("API_PASS", "verno");

    final var apiUser = User.withUsername(user)
            .password(encoder.encode(pass))
            .roles("API")
            .build();

    return new InMemoryUserDetailsManager(apiUser);
  }

  @Bean
  @Nonnull
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  private static String readEnvOrDefault(@Nonnull final String key, @Nonnull final String defaultValue) {
    final var value = System.getenv(key);
    if (value == null) return defaultValue;

    final var trimmed = value.trim();
    return trimmed.isEmpty() ? defaultValue : trimmed;
  }
}