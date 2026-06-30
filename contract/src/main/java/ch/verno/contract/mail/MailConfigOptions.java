package ch.verno.contract.mail;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record MailConfigOptions(@Nullable MailOrigin mailOrigin) {

  @Nonnull
  public static MailConfigOptions empty() {
    return new MailConfigOptions(null);
  }


  public enum MailOrigin {
    TENANT_CONFIG,
    ENV // ONLY USE FOR INTERNAL API CALLS (e.g. get in touch dialog from landing page) !!!!!
  }

}
