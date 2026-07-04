package ch.verno.gateway.security;

import ch.verno.common.lib.api.ApiUrl;
import ch.verno.contract.endpoint.properties.api.ApiConfigResource;
import ch.verno.lib.Publ;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * All gateway-level {@link SecurityFilterChain} configuration in one place, ordered by matcher
 * specificity (most specific first):
 *
 * <ol>
 *   <li>{@code /public/api/v1/auth/**} — token-gated public resources (scoped access token)</li>
 *   <li>{@code /public/api/v1/**} — fully open public resources</li>
 *   <li>{@code /internal/api/v1/**} — server-to-server only, Basic Auth</li>
 *   <li>{@code /api/v1/**} — external-facing authenticated API, Basic Auth</li>
 * </ol>
 */
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
                                                            @Nonnull final ObjectPostProcessor<Object> objectPostProcessor) {
    final var provider = new DaoAuthenticationProvider(gatewayUserDetailsService);
    provider.setPasswordEncoder(passwordEncoder);

    final var builder = new AuthenticationManagerBuilder(objectPostProcessor);
    builder.authenticationProvider(provider);
    return builder.build();
  }

  @Bean
  @Order(0)
  public SecurityFilterChain publicAuthFilterChain(@Nonnull final HttpSecurity http,
                                                   @Nonnull final ResourceAccessFilter resourceAccessFilter) {

    http
            .securityMatcher(ApiUrl.PUBLIC_AUTH_BASE_API + Publ.API_ANYTHING)
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(resourceAccessFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

    return http.build();
  }

  @Bean
  @Order(1)
  public SecurityFilterChain publicOpenFilterChain(@Nonnull final HttpSecurity http,
                                                   @Nonnull final CorsConfigurationSource apiCorsSource) {
    http
            .securityMatcher(ApiUrl.PUBLIC_BASE_API + Publ.API_ANYTHING)
            .cors(cors -> cors.configurationSource(apiCorsSource))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

    return http.build();
  }

  @Bean
  @Order(2)
  public SecurityFilterChain internalFilterChain(@Nonnull final HttpSecurity http,
                                                 @Nonnull final AuthenticationManager gatewayAuthenticationManager) {

    http
            .securityMatcher(ApiUrl.INTERNAL_BASE_API + Publ.API_ANYTHING)
            .csrf(AbstractHttpConfigurer::disable)
            .authenticationManager(gatewayAuthenticationManager)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  @Order(3)
  public SecurityFilterChain apiFilterChain(@Nonnull final HttpSecurity http,
                                            @Nonnull final AuthenticationManager gatewayAuthenticationManager,
                                            @Nonnull final CorsConfigurationSource apiCorsSource) {

    http
            .securityMatcher(ApiUrl.BASE_API + Publ.API_ANYTHING)
            .cors(cors -> cors.configurationSource(apiCorsSource))
            .csrf(AbstractHttpConfigurer::disable)
            .authenticationManager(gatewayAuthenticationManager)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults());

    return http.build();
  }
}