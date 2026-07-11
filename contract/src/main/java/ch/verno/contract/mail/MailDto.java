package ch.verno.contract.mail;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record MailDto(@Nullable String fromName,
                      @Nonnull String from,
                      @Nonnull String recipient,
                      @Nonnull MailContentDto contentDto,
                      @Nullable MailConfigOptions options) {

  @Nonnull
  public static MailDto simple(@Nonnull final String from,
                               @Nonnull final String recipient,
                               @Nonnull final String subject,
                               @Nonnull final String content) {
    final var contentDto = new MailContentDto(subject, content, null);
    return new MailDto(
            null,
            from,
            recipient,
            contentDto,
            MailConfigOptions.empty()
    );
  }

}