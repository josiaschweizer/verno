package ch.verno.server.bo.env;

import ch.verno.common.environment.EnvironmentUtil;
import ch.verno.common.exceptions.lib.EnvironmentVariableNotFound;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class EnvironmentVariableBo {

  @Nonnull private final Dotenv dotEnv;

  protected EnvironmentVariableBo() {
    // Find project root by looking for pom.xml or .env file
    final var directory = EnvironmentUtil.findProjectRoot();

    dotEnv = Dotenv.configure()
            .directory(directory)
            .ignoreIfMissing()
//            .systemProperties() // TODO do we have to re-active that?
            .load();
  }


  @Nonnull
  public String getEnv(@Nonnull final String key) {
    final var env = getEnvNullable(key);

    if (env == null) {
      throw new EnvironmentVariableNotFound("Environment variable not found: " + key);
    }

    return env;
  }

  @Nullable
  public String getEnvNullable(@Nonnull final String key) {
    return dotEnv.get(key);
  }

  @Nonnull
  public String getEnvOrDefault(@Nonnull final String key,
                                @Nonnull final String defaultValue) {
    final var envNullable = getEnvNullable(key);
    return envNullable != null ? envNullable : defaultValue;
  }

}
