package ch.verno.contract.mail;

import jakarta.annotation.Nonnull;

public record MailContentDto(@Nonnull String subject,
                             @Nonnull String content) {
}
