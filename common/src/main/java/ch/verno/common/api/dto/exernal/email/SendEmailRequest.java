package ch.verno.common.api.dto.exernal.email;

import jakarta.annotation.Nonnull;

public record SendEmailRequest(@Nonnull String from,
                               @Nonnull String to,
                               @Nonnull String subject,
                               @Nonnull String message) {
}
