package ch.verno.common.server.mail;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record MailConfigOptions(@Nullable MailOrigin mailOrigin) {

  @Nonnull
  public static MailConfigOptions empty() {
    return new MailConfigOptions(null);
  }

}
