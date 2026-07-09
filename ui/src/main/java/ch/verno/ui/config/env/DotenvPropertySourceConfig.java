package ch.verno.ui.config.env;

import ch.verno.common.environment.EnvironmentUtil;
import ch.verno.lib.Publ;
import ch.verno.lib.properties.ApplicationPropertiesConstants;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

@Configuration
public class DotenvPropertySourceConfig {

  @Value(ApplicationPropertiesConstants.VERNO_UI_LOADED_ENV)
  private Set<String> loadedEnvs;

  @Bean
  public static PropertySourcesPlaceholderConfigurer properties(@Nonnull final Environment environment) {
    final var dotenv = getDotEnv();
    final var loadedEnvs = resolveLoadedEnvs(environment);

    final var props = new Properties();
    dotenv.entries().forEach(e -> {
      if (loadedEnvs.contains(e.getKey())) {
        props.setProperty(e.getKey(), e.getValue());
      }
    });

    final var configurer = new PropertySourcesPlaceholderConfigurer();
    configurer.setProperties(props);
    return configurer;
  }

  @Nonnull
  private static List<String> resolveLoadedEnvs(@Nonnull final Environment environment) {
    final var raw = environment.getProperty(
            ApplicationPropertiesConstants.VERNO_UI_LOADED_ENV,
            Publ.EMPTY_STRING
    );
    return Arrays.asList(raw.split(Publ.COMMA));
  }

  @Nonnull
  private static Dotenv getDotEnv() {
    // Find project root by looking for pom.xml or .env file
    final var directory = EnvironmentUtil.findProjectRoot();

    return Dotenv.configure()
            .directory(directory)
            .ignoreIfMissing()
            .load();
  }

}
