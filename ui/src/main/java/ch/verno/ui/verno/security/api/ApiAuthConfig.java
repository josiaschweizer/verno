package ch.verno.ui.verno.security.api;

import ch.verno.rpc.properties.application.ApplicationProperties;
import ch.verno.rpc.properties.env.EnvProperties;
import com.google.inject.Injector;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class ApiAuthConfig {

  @Nonnull
  @Bean("apiUserDetailsService")
  public UserDetailsService apiUserDetailsService(@Nonnull final Injector injector) {
    final var applicationProperties = injector.getInstance(ApplicationProperties.class);
    final var apiUsername = applicationProperties.getApiUsername();
    final var envProperties = injector.getInstance(EnvProperties.class);
    final var encodedApiPassword = envProperties.getEncodedApiPassword();

    return new InMemoryUserDetailsManager(
            User.builder()
                    .username(apiUsername)
                    .password(encodedApiPassword)
                    .roles("API")
                    .build()
    );
  }

  @Nonnull
  @Bean("apiAuthenticationManager")
  @Primary
  public AuthenticationManager apiAuthenticationManager(@Nonnull final PasswordEncoder passwordEncoder,
                                                        @Qualifier("apiUserDetailsService")
                                                        @Nonnull final UserDetailsService apiUserDetailsService) {
    final var provider = new DaoAuthenticationProvider(apiUserDetailsService); //TODO 1 delete password encoder in frontend?
    provider.setPasswordEncoder(passwordEncoder);

    return new ProviderManager(provider);
  }

  @Nonnull
  @Bean("vaadinAuthenticationManager")
  public AuthenticationManager vaadinAuthenticationManager(@Nonnull final PasswordEncoder passwordEncoder,
                                                           @Qualifier("appUserService")
                                                           @Nonnull final UserDetailsService userDetailsService) {
    final var provider = new DaoAuthenticationProvider(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);

    return new ProviderManager(provider);
  }
}