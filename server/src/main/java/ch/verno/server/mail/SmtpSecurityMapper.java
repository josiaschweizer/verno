package ch.verno.server.mail;

import ch.verno.common.type.mail.SmtpSecurity;
import jakarta.annotation.Nonnull;
import org.simplejavamail.api.mailer.config.TransportStrategy;

public final class SmtpSecurityMapper {

  @Nonnull
  public static TransportStrategy toTransportStrategy(@Nonnull final String stringValue) {
    final var smtpSecurity = SmtpSecurity.fromString(stringValue);
    return toTransportStrategy(smtpSecurity);
  }

  @Nonnull
  public static TransportStrategy toTransportStrategy(@Nonnull final SmtpSecurity smtpSecurity) {
    return switch (smtpSecurity) {
      case STARTTLS -> TransportStrategy.SMTP_TLS;
      case SSL_TLS -> TransportStrategy.SMTPS;
      case NONE -> TransportStrategy.SMTP;
    };
  }

}