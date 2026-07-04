package ch.verno.gateway.security;

import ch.verno.lib.Publ;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class ApiCorsConfig {

  //TODO CONSTANTS!!!

  @Bean
  public CorsConfigurationSource apiCorsSource() {
    final var config = new CorsConfiguration();
    config.setAllowCredentials(true);

    config.setAllowedOrigins(List.of(
            "https://www.verno-app.ch",
            "https://verno-app.ch",
            "https://payment.verno-app.ch",
            "http://localhost:5173"
    ));

    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    config.setAllowedHeaders(List.of("*"));
    config.setExposedHeaders(List.of("Authorization"));

    final var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration(Publ.API_ANYTHING, config);
    return source;
  }

}
