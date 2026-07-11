package ch.verno.server.config.security;

import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
public class ServerSecurityConfig {

  @Bean
  @Order(1)
  public SecurityFilterChain rpcFilterChain(@Nonnull final HttpSecurity http,
                                            @Nonnull final InternalRpcAuthFilter internalRpcAuthFilter) {
    http.securityMatcher(Publ.RPC, Publ.RPC + Publ.API_ANYTHING)
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
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