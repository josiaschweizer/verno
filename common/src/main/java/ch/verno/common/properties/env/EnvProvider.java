package ch.verno.common.properties.env;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public interface EnvProvider {

  @Nonnull
  String getEnv(@Nonnull String key);

  @Nullable
  String getEnvNullable(@Nonnull String key);

  @Nonnull
  String getEnvOrDefault(@Nonnull String key,
                         @Nonnull String defaultValue);

}
