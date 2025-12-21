package ch.verno.ui.verno.participant;

import jakarta.annotation.Nonnull;

public record Participant(@Nonnull String id,
                          @Nonnull String firstName,
                          @Nonnull String lastName,
                          int age,
                          @Nonnull String email,
                          @Nonnull String phone) {
}
