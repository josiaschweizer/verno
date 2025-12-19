package ch.verno.server.persistence.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "app_user")
public class AppUserEntity {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @Nonnull
  @Column(name = "email", nullable = false, unique = true, length = 255)
  private String email;

  @Nonnull
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  protected AppUserEntity() {
    // JPA only
  }

  public static @Nonnull AppUserEntity of(
      @Nonnull final UUID id,
      @Nonnull final String email,
      @Nonnull final Instant createdAt
  ) {
    final var e = new AppUserEntity();
    e.id = id;
    e.email = email;
    e.createdAt = createdAt;
    return e;
  }

  public @Nonnull UUID getId() {
    return id;
  }

  public @Nonnull String getEmail() {
    return email;
  }

  public void setEmail(@Nonnull final String email) {
    this.email = email;
  }

  public @Nonnull Instant getCreatedAt() {
    return createdAt;
  }
}