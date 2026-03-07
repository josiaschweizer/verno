package ch.verno.common.lib.mail;

import jakarta.annotation.Nonnull;

public record MailContentDto(@Nonnull String subject,
                             @Nonnull String content) {
}
