package ch.verno.domain.model.user;

import jakarta.annotation.Nonnull;

import java.time.Instant;
import java.util.UUID;

public final class AppUser {

  @Nonnull
  private final UUID id;

  @Nonnull
  private final String email;

  @Nonnull
  private final Instant createdAt;

  public AppUser(
      @Nonnull final UUID id,
      @Nonnull final String email,
      @Nonnull final Instant createdAt
  ) {
    this.id = id;
    this.email = email;
    this.createdAt = createdAt;
  }

  public @Nonnull UUID getId() {
    return id;
  }

  public @Nonnull String getEmail() {
    return email;
  }

  public @Nonnull Instant getCreatedAt() {
    return createdAt;
  }
}