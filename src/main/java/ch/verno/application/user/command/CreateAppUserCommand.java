package ch.verno.application.user.command;

import jakarta.annotation.Nonnull;

public record CreateAppUserCommand(
    @Nonnull String email
) {
}