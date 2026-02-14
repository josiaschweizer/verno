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
  public SecurityFilterChain vaadinFilterChain(@Nonnull HttpSecurity http,
                                               @Qualifier("vaadinAuthenticationManager") @Nonnull AuthenticationManager vaadinAuthenticationManager) {
    http
            .csrf(AbstractHttpConfigurer::disable)
            .authenticationManager(vaadinAuthenticationManager)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(ApiUrl.TEMP_FILE_REPORT + "/**").permitAll()
                    .requestMatchers(ApiUrl.TEMP_FILE_IMPORT + "/**").permitAll()
                    .requestMatchers(ApiUrl.TEMP_FILE_EXPORT + "/**").permitAll()
                    .requestMatchers(ApiUrl.FILES + "/**").permitAll()
            )
            .with(VaadinSecurityConfigurer.vaadin(), configurer ->
                    configurer.loginView(LoginView.class)
            )
            .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

    return http.build();
  }
}