package ch.verno.server.mail;

import ch.verno.common.db.service.intern.mail.IMailConfigService;
import ch.verno.common.db.service.intern.mail.IMailLogService;
import ch.verno.common.exceptions.server.mail.SendMailException;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;

public class MailerUtil {

  private MailerUtil() {
  }

  @Nonnull
  public static Mailer createMailer(@Nonnull final GlobalInterface globalInterface) {
    final var mailConfigService = globalInterface.getService(IMailConfigService.class);
    final var mailConfig = mailConfigService.getConfigForCurrentTenant();

    return MailerBuilder
            .withSMTPServer(
                    mailConfig.getSmtpHost(),
                    mailConfig.getSmtpPort(),
                    mailConfig.getSmtpUsername(),
                    mailConfig.getDecodedSmtpPassword()
            )
            .withTransportStrategy(mailConfig.getSmtpSecurity().toTransportStrategy())
            .withSessionTimeout(5000)
            .buildMailer();
  }

  public static void sendMail(@Nonnull final GlobalInterface globalInterface,
                              @Nonnull final Email email) {
    boolean valid = true;
    String errorMsg = null;

    try (final var mailer = MailerUtil.createMailer(globalInterface)) {
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
