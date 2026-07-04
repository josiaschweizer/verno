package ch.verno.contract.mail.placeholder.base;

import jakarta.annotation.Nonnull;

public interface Placeholder {

  @Nonnull
  String getNameKey();

  @Nonnull
  String getValue();

}
