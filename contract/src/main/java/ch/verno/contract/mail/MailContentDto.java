package ch.verno.contract.mail;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record MailContentDto(@Nonnull String subject,
                             @Nonnull String content,
                             @Nullable MailContentType mailContentType) {

  @Nonnull
  public static MailContentDto plain(@Nonnull final String subject,
                                     @Nonnull final String content) {
    return new MailContentDto(subject, content, MailContentType.PLAIN);
  }


  public enum MailContentType {
    HTML,
    PLAIN
  }

}
