package ch.verno.common.db.enums.mail;

import jakarta.annotation.Nonnull;
import org.simplejavamail.api.mailer.config.TransportStrategy;

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

  public long getId() {
    return id;
  }

  @Nonnull
  public String getDisplayName() {
    return displayName;
  }

  @Nonnull
  public TransportStrategy toTransportStrategy() {
    return switch (this) {
      case STARTTLS -> TransportStrategy.SMTP_TLS;
      case SSL_TLS -> TransportStrategy.SMTPS;
      case NONE -> TransportStrategy.SMTP;
    };
  }
}