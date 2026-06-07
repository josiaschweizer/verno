package ch.verno.server.mail;

import ch.verno.common.server.service.intern.mail.IMailConfigService;
import ch.verno.common.server.service.intern.mail.IMailLogService;
import ch.verno.common.db.type.mail.SmtpSecurity;
import ch.verno.common.exceptions.server.mail.SendMailException;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.server.mail.MailOrigin;
import ch.verno.publ.Publ;
import ch.verno.publ.VernoSecrets;
import jakarta.annotation.Nonnull;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.mailer.MailerBuilder;

public class MailerUtil {

  private MailerUtil() {
  }

  @Nonnull
  public static Mailer createMailer(@Nonnull final GlobalInterface globalInterface,
                                    @Nonnull final MailOrigin mailOrigin) {
    String smtpHost;
    int smtpPort;
    String smtpUser;
    String smtpPassword;
    TransportStrategy transportationStrategy;

    if (mailOrigin == MailOrigin.TENANT_CONFIG) {
      final var mailConfigService = globalInterface.getService(IMailConfigService.class);
      final var mailConfig = mailConfigService.getConfigForCurrentTenant();

      smtpHost = mailConfig.getSmtpHost();
      smtpPort = mailConfig.getSmtpPort();
      smtpUser = mailConfig.getSmtpUsername();
      smtpPassword = mailConfig.getDecodedSmtpPassword();
      transportationStrategy = mailConfig.getSmtpSecurity().toTransportStrategy();
    } else if (mailOrigin == MailOrigin.ENV) {
      final var envProperties = globalInterface.getEnvProperties();

      smtpHost = envProperties.getEnv(VernoSecrets.SMTP_HOST);
      smtpPort = Integer.parseInt(envProperties.getEnv(VernoSecrets.SMTP_PORT));
      smtpUser = envProperties.getEnv(VernoSecrets.SMTP_USER);
      smtpPassword = envProperties.getEnv(VernoSecrets.SMTP_PASS);
      transportationStrategy = SmtpSecurity.fromString(envProperties.getEnv(VernoSecrets.SMTP_SECURITY)).toTransportStrategy();
    } else {
      smtpHost = Publ.EMPTY_STRING;
      smtpPort = Publ.ZERO;
      smtpUser = Publ.EMPTY_STRING;
      smtpPassword = Publ.EMPTY_STRING;
      transportationStrategy = TransportStrategy.SMTP_TLS;
    }

    return MailerBuilder
            .withSMTPServer(
                    smtpHost,
                    smtpPort,
                    smtpUser,
                    smtpPassword
            )
            .withTransportStrategy(transportationStrategy)
            .withSessionTimeout(5000)
            .buildMailer();
  }

  public static void sendMail(@Nonnull final GlobalInterface globalInterface,
                              @Nonnull final Email email) {
    sendMail(globalInterface, email, MailOrigin.TENANT_CONFIG);
  }

  public static void sendMail(@Nonnull final GlobalInterface globalInterface,
                              @Nonnull final Email email,
                              @Nonnull final MailOrigin mailOrigin) {
    boolean valid = true;
    String errorMsg = null;

    try (final var mailer = MailerUtil.createMailer(globalInterface, mailOrigin)) {
      mailer.sendMail(email, true).get();
    } catch (Exception e) {
      valid = false;
      errorMsg = e.getMessage();
      throw new SendMailException("Failed to send mail", e);
    } finally {
      final var mailLogService = globalInterface.getService(IMailLogService.class);
      if (valid) {
        mailLogService.logSent(email, Publ.EMPTY_STRING);
      } else {
        mailLogService.logFailed(email, errorMsg != null ? errorMsg : "Unknown error");
      }
    }
  }

}
