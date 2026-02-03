package ch.verno.server;

import ch.verno.server.mandant.MandantProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MandantProperties.class)
public class ServerConfig {
}