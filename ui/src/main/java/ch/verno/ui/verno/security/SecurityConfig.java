package ch.verno.ui.verno.security;

import ch.verno.publ.ApiUrl;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Profile("prod")
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  @Nonnull
  SecurityFilterChain securityFilterChain(@Nonnull HttpSecurity http) throws Exception {

    http.authorizeHttpRequests(auth -> auth
            .requestMatchers(ApiUrl.TEMP_FILE_REPORT + "/**").permitAll()
            .requestMatchers(ApiUrl.TEMP_FILE_IMPORT + "/**").permitAll()
            .requestMatchers(ApiUrl.TEMP_FILE_EXPORT + "/**").permitAll()
            .requestMatchers("/api/v1/tenants").permitAll()
    );

    http = http.with(VaadinSecurityConfigurer.vaadin(), configurer -> {
      configurer.loginView(LoginView.class);
    });

    http.csrf(csrf -> csrf
            .ignoringRequestMatchers(ApiUrl.TEMP_FILE_REPORT + "/**")
            .ignoringRequestMatchers(ApiUrl.TEMP_FILE_IMPORT + "/**")
            .ignoringRequestMatchers(ApiUrl.TEMP_FILE_EXPORT + "/**")
            .ignoringRequestMatchers("/api/v1/tenants")
    );

    http.headers(headers -> headers
            .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
    );

    return http.build();
  }

  @Bean
  @Nonnull
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}