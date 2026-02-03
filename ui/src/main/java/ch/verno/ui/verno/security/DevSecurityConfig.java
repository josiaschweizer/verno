package ch.verno.ui.verno.security;

import ch.verno.publ.ApiUrl;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("dev")
@EnableWebSecurity
public class DevSecurityConfig {

  @Bean
  SecurityFilterChain devSecurityFilterChain(@Nonnull HttpSecurity http) throws Exception{
    http.authorizeHttpRequests(auth -> auth
            .requestMatchers(ApiUrl.TEMP_FILE_REPORT + "/**").permitAll()
            .requestMatchers(ApiUrl.TEMP_FILE_IMPORT + "/**").permitAll()
            .requestMatchers(ApiUrl.TEMP_FILE_EXPORT + "/**").permitAll()
    );

    http = http.with(VaadinSecurityConfigurer.vaadin(), configurer -> {
      configurer.loginView(LoginView.class);
    });

    http.csrf(csrf -> csrf
            .ignoringRequestMatchers(ApiUrl.TEMP_FILE_REPORT + "/**")
            .ignoringRequestMatchers(ApiUrl.TEMP_FILE_IMPORT + "/**")
            .ignoringRequestMatchers(ApiUrl.TEMP_FILE_EXPORT + "/**")
            .ignoringRequestMatchers(ApiUrl.DEBUG + "/**")
    );

    http.headers(headers -> headers
            .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
    );

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}