package ch.verno.common.properties.configprovider;

import jakarta.annotation.Nonnull;

public interface VernoApplicationConfigProvider {

  @Nonnull
  String getRunMode();

}
