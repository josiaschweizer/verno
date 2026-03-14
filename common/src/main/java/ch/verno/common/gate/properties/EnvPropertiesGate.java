package ch.verno.common.gate.properties;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public interface EnvPropertiesGate {

  @Nonnull
  String getEnv(@Nonnull String key);

  @Nullable
  String getEnvNullable(@Nonnull String key);

  @Nonnull
  String getEnvOrDefault(@Nonnull String key,
                         @Nonnull String defaultValue);

}
