package ch.verno.ui.config;

import jakarta.annotation.Nonnull;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.flyway.autoconfigure.FlywayProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(FlywayProperties.class)
public class FlywayManualConfig {

  @Bean(initMethod = "migrate")
  @ConditionalOnProperty(name = "spring.flyway.enabled", havingValue = "true", matchIfMissing = true)
  public Flyway flyway(@Nonnull final DataSource dataSource,
                       @Nonnull final ObjectProvider<FlywayProperties> propertiesProvider) {
    final var properties = propertiesProvider.getIfAvailable(FlywayProperties::new);

    return Flyway.configure()
            .dataSource(dataSource)
            .locations(properties.getLocations().toArray(String[]::new))
            .baselineOnMigrate(properties.isBaselineOnMigrate())
            .baselineVersion(properties.getBaselineVersion())
            .schemas(properties.getSchemas().toArray(String[]::new))
            .table(properties.getTable())
            .load();
  }
}
