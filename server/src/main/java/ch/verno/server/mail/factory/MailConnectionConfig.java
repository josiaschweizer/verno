package ch.verno.server.mail.factory;

import jakarta.annotation.Nonnull;
import org.simplejavamail.api.mailer.config.TransportStrategy;

public record MailConnectionConfig(@Nonnull String smtpHost,
                                   int smtpPort,
                                   @Nonnull String smtpUsername,
                                   @Nonnull String smtpPassword,
                                   @Nonnull TransportStrategy transportStrategy) {
}