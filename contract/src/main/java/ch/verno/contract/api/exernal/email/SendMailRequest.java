package ch.verno.contract.api.exernal.email;

import jakarta.annotation.Nonnull;

public record SendMailRequest(@Nonnull String from,
                              @Nonnull String to,
                              @Nonnull String subject,
                              @Nonnull String message) {
}
