package ch.verno.contract.mail;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record MailContentDto(@Nonnull String subject,
                             @Nonnull String content,
                             @Nullable MailContentType mailContentType) {


  public enum MailContentType {
    HTML,
    PLAIN
  }

}
