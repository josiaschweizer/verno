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

  @Nonnull
  public static AppUserEntity of(
      @Nonnull final UUID id,
      @Nonnull final String email,
      @Nonnull final Instant createdAt
  ) {
    final var entity = new AppUserEntity();
    entity.id = id;
    entity.email = email;
    entity.createdAt = createdAt;
    return entity;
  }

  @Nonnull
  public UUID getId() {
    return id;
  }

  @Nonnull
  public String getEmail() {
    return email;
  }

  public void setEmail(@Nonnull final String email) {
    this.email = email;
  }

  @Nonnull
  public Instant getCreatedAt() {
    return createdAt;
  }
}