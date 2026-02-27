package ch.verno.server.mail;

import ch.verno.common.gate.GlobalInterface;
import jakarta.annotation.Nonnull;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.mailer.MailerBuilder;

public class MailFactory {

  private MailFactory() {
  }

  @Nonnull
  public static Mailer createMailer(@Nonnull final GlobalInterface globalInterface) {
    final var envProperties = globalInterface.getEnvironmentVariableProperties();
    final var host = envProperties.getEnv("SMTP_HOST");
    final var port = Integer.parseInt(envProperties.getEnvOrDefault("SMTP_PORT", "587"));
    final var user = envProperties.getEnv("SMTP_USER");
    final var pass = envProperties.getEnv("SMTP_PASS");
    final var tls = Boolean.parseBoolean(envProperties.getEnvOrDefault("SMTP_TLS", "true"));

    return MailerBuilder
            .withSMTPServer(host, port, user, pass)
            .withTransportStrategy(tls
                    ? TransportStrategy.SMTP_TLS
                    : TransportStrategy.SMTP)
            .buildMailer();
  }

}
