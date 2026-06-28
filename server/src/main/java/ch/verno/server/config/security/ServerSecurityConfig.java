package ch.verno.server.config.security;

import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
public class ServerSecurityConfig {

  @Bean
  @Nonnull
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Order(1)
  public SecurityFilterChain rpcFilterChain(@Nonnull final HttpSecurity http,
                                            @Nonnull final InternalRpcAuthFilter internalRpcAuthFilter) {
    http.securityMatcher("/rpc", "/rpc/**")
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .addFilterBefore(internalRpcAuthFilter, AnonymousAuthenticationFilter.class);


    return http.build();
  }

  @Bean
  @Order(2)
  public SecurityFilterChain defaultFilterChain(@Nonnull final HttpSecurity http) {
    http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

    return http.build();
  }
}