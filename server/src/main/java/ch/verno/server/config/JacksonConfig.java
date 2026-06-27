package ch.verno.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class JacksonConfig {

  @Bean
  @Nonnull
  public ObjectMapper getObjectMapper() {
    return new ObjectMapper().findAndRegisterModules();
  }

}
