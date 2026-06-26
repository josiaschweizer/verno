package ch.verno.ui.verno.security.api;

import ch.verno.contract.gateway.ApiUrl;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Qualifier;
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
                                            @Qualifier("apiAuthenticationManager") @Nonnull final AuthenticationManager apiAuthenticationManager) throws Exception {
    final var resolveBillingAccessToken = ApiUrl.BILLING_ACCESS_TOKEN + ApiUrl.RESOLVE_ACCESS_TOKEN;
    final var startStripeSession = ApiUrl.BILLING_SESSION + ApiUrl.START_STRIPE_SESSION;

    http
            .securityMatcher("/api/**")
            .cors(cors -> cors.configurationSource(apiCorsSource))
            .csrf(AbstractHttpConfigurer::disable)
            .authenticationManager(apiAuthenticationManager)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(ApiUrl.BILLING_WEBHOOK, ApiUrl.BILLING_WEBHOOK + "/**").permitAll()
                    .requestMatchers(resolveBillingAccessToken, resolveBillingAccessToken + "/**").permitAll()
                    .requestMatchers(startStripeSession, startStripeSession + "/**").permitAll()
                    .requestMatchers(ApiUrl.TENANTS, ApiUrl.TENANTS + "/**").authenticated()
                    .requestMatchers(ApiUrl.EMAIL, ApiUrl.EMAIL + "/**").authenticated()
                    .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

    return http.build();
  }
}