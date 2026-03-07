package ch.verno.common.gate.properties;

import ch.verno.common.exceptions.lib.EnvironmentVariableNotFound;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class EnvironmentVariableProperties {

  @Nonnull private final Dotenv dotEnv;

  public EnvironmentVariableProperties() {
    // Find project root by looking for pom.xml or .env file
    String directory = findProjectRoot();

    dotEnv = Dotenv.configure()
            .directory(directory)
            .ignoreIfMissing()
            .systemProperties()
            .load();
  }

  private String findProjectRoot() {
    Path currentPath = Paths.get(System.getProperty("user.dir"));

    while (currentPath != null) {
      final var envFile = currentPath.resolve(".env").toFile();
      final var pomFile = currentPath.resolve("pom.xml").toFile();

      if (envFile.exists()) {
        return currentPath.toString();
      }

      if (pomFile.exists()) {
        final var parent = currentPath.getParent();
        if (parent != null && parent.resolve("pom.xml").toFile().exists()) {
          currentPath = parent;
          continue;
        } else {
          return currentPath.toString();
        }
      }

      currentPath = currentPath.getParent();
    }

    return System.getProperty("user.dir");
  }

  @Nonnull
  public String getEnv(@Nonnull String key) {
    final var env = getEnvNullable(key);

    if (env == null) {
      throw new EnvironmentVariableNotFound("Environment variable not found: " + key);
    }

    return env;
  }

  @Nullable
  public String getEnvNullable(@Nonnull String key) {
    return dotEnv.get(key);
  }

  @Nonnull
  public String getEnvOrDefault(@Nonnull final String key,
                                @Nonnull final String defaultValue) {
    final var envNullable = getEnvNullable(key);
    return envNullable != null ? envNullable : defaultValue;
  }

}
