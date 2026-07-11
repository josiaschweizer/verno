package ch.verno.gateway.config.env;

import ch.verno.common.environment.EnvironmentUtil;
import ch.verno.lib.Publ;
import ch.verno.lib.properties.ApplicationPropertiesConstants;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Configuration
public class GatewayPropertySourceConfig {

  @Bean
  public static PropertySourcesPlaceholderConfigurer properties(@Nonnull final Environment environment) {
    final var dotenv = getDotEnv();
    final var loadedEnvs = resolveLoadedEnvs(environment);

    final var props = new Properties();
    for (final var key : loadedEnvs) {
      final var fromDotenv = dotenv.get(key);

      final var value = Optional.ofNullable(fromDotenv).orElseGet(() -> System.getenv(key));
      if (value != null) {
        props.setProperty(key, value);
      }
    }

    final var configurer = new PropertySourcesPlaceholderConfigurer();
    configurer.setProperties(props);
    return configurer;
  }

  @Nonnull
  private static List<String> resolveLoadedEnvs(@Nonnull final Environment environment) {
    final var raw = environment.getProperty(
            ApplicationPropertiesConstants.GATEWAY_LOADED_ENV,
            Publ.EMPTY_STRING
    );
    return Arrays.asList(raw.split(Publ.COMMA));
  }

  @Nonnull
  private static Dotenv getDotEnv() {
    final var directory = EnvironmentUtil.findProjectRoot();

    return Dotenv.configure()
            .directory(directory)
            .systemProperties()
            .ignoreIfMissing()
            .load();
  }

}