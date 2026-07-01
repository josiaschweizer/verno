package ch.verno.gateway.security;

import ch.verno.contract.endpoint.properties.api.ApiConfigResource;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class GatewaySecurityConfig {

  @Nonnull private final String apiUsername;
  @Nonnull private final String apiPassword;

  public GatewaySecurityConfig(@Nonnull final RpcFactory rpcFactory) {
    final var apiProperties = rpcFactory.create(ApiConfigResource.class);
    this.apiUsername = apiProperties.getApiUsername();
    this.apiPassword = apiProperties.getApiPassword();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public UserDetailsService gatewayUserDetailsService(@Nonnull final PasswordEncoder passwordEncoder) {
    final var user = User.withUsername(apiUsername)
            .password(passwordEncoder.encode(apiPassword))
            .roles("API")
            .build();
    return new InMemoryUserDetailsManager(user);
  }

  @Bean
  public AuthenticationManager gatewayAuthenticationManager(@Nonnull final UserDetailsService gatewayUserDetailsService,
                                                            @Nonnull final PasswordEncoder passwordEncoder,
                                                            @Nonnull final ObjectPostProcessor<Object> objectPostProcessor) throws Exception {
    final var provider = new DaoAuthenticationProvider(gatewayUserDetailsService);
    provider.setPasswordEncoder(passwordEncoder);

    final var builder = new AuthenticationManagerBuilder(objectPostProcessor);
    builder.authenticationProvider(provider);
    return builder.build();
  }

  @Bean
  public SecurityFilterChain gatewayFilterChain(@Nonnull final HttpSecurity http,
                                                @Nonnull final AuthenticationManager gatewayAuthenticationManager) throws Exception {
    http
            .securityMatcher("/internal/api/v1/**")
            .csrf(AbstractHttpConfigurer::disable)
            .authenticationManager(gatewayAuthenticationManager)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults());

    return http.build();
  }
}