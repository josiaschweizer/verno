package ch.verno.common.type.mail;

import jakarta.annotation.Nonnull;

public enum SmtpSecurity {
  STARTTLS(0, "STARTTLS"),
  SSL_TLS(1, "SSL/TLS"),
  NONE(-1, "None");

  private final int id;
  @Nonnull private final String displayName;

  SmtpSecurity(final int id,
               @Nonnull final String displayName) {
    this.id = id;
    this.displayName = displayName;
  }

  @Nonnull
  public static SmtpSecurity fromId(final long id) {
    for (final SmtpSecurity security : values()) {
      if (security.id == id) {
        return security;
      }
    }

    throw new IllegalArgumentException("Invalid SmtpSecurity id: " + id);
  }

  @Nonnull
  public static SmtpSecurity fromString(@Nonnull final String name) {
    for (final var value : values()) {
      if (value.name().equals(name)) {
        return value;
      }
    }

    throw new IllegalArgumentException("Invalid SmtpSecurity name: " + name);
  }

  public long getId() {
    return id;
  }

  @Nonnull
  public String getDisplayName() {
    return displayName;
  }

}