package ch.verno.common.environment;

import jakarta.annotation.Nonnull;

import java.nio.file.Path;
import java.nio.file.Paths;

public class EnvironmentUtil {

  public static final String USER_DIR = "user.dir";
  public static final String ENV = ".env";
  public static final String POM_XML = "pom.xml";

  @Nonnull
  public static String findProjectRoot() {
    Path currentPath = Paths.get(System.getProperty(USER_DIR));

    while (currentPath != null) {
      final var envFile = currentPath.resolve(ENV).toFile();
      final var pomFile = currentPath.resolve(POM_XML).toFile();

      if (envFile.exists()) {
        return currentPath.toString();
      }

      if (pomFile.exists()) {
        final var parent = currentPath.getParent();
        if (parent != null && parent.resolve(POM_XML).toFile().exists()) {
          currentPath = parent;
          continue;
        } else {
          return currentPath.toString();
        }
      }

      currentPath = currentPath.getParent();
    }

    return System.getProperty(USER_DIR);
  }

}
