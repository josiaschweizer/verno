package ch.verno.ui.verno.security.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class ApiCorsConfig {

  @Bean
  public CorsConfigurationSource apiCorsSource() {
    final var config = new CorsConfiguration();
    config.setAllowedOriginPatterns(List.of("http://localhost:*"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);
    config.setExposedHeaders(List.of("Authorization"));

    final var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", config);
    return source;
  }
}