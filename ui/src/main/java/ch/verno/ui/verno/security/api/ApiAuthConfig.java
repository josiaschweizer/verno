package ch.verno.ui.verno.security.api;

import com.google.inject.AbstractModule;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

public class ApiAuthConfig extends AbstractModule {

  @Value("${verno.api.username:verno}")
  private String apiUsername;

  @Value("${verno.api.password:verno}")
  private String apiPassword;

  @Bean
  public UserDetailsService apiUserDetailsService(@Nonnull final PasswordEncoder passwordEncoder) {
    return new InMemoryUserDetailsManager(
            User.builder()
                    .username(apiUsername)
                    .password(passwordEncoder.encode(apiPassword))
                    .roles("API")
                    .build()
    );
  }

  @Bean
  @Primary
  public AuthenticationManager apiAuthenticationManager(@Nonnull final PasswordEncoder passwordEncoder,
                                                        @Qualifier("apiUserDetailsService") @Nonnull UserDetailsService apiUserDetailsService) {
    final var provider = new DaoAuthenticationProvider(apiUserDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return new ProviderManager(provider);
  }

  @Bean
  public AuthenticationManager vaadinAuthenticationManager(@Nonnull final PasswordEncoder passwordEncoder,
                                                           @Qualifier("appUserService") @Nonnull UserDetailsService userDetailsService) {
    final var provider = new DaoAuthenticationProvider(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return new ProviderManager(provider);
  }
}