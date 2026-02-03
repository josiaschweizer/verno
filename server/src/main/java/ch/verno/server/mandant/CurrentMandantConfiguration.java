package ch.verno.server.mandant;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CurrentMandantConfiguration {

  @Bean
  public CurrentMandant currentMandant() {
    return new CurrentMandant();
  }
}