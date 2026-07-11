package ch.verno.contract.endpoint.properties.env;

import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

@RpcEndpoint
public interface EnvResource {

  @Nonnull
  String getEnv(@Nonnull String key);

  @Nonnull
  String getEnv(@Nonnull String key, @Nonnull String defaultValue);

  @Nonnull
  String getEnvNullable(@Nonnull String key);

  @Nonnull
  String encodeString(@Nonnull String value);

}
