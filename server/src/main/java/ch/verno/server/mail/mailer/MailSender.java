package ch.verno.server.mail.mailer;

import ch.verno.common.exceptions.server.mail.SendMailException;
import ch.verno.contract.mail.MailConfigOptions;
import ch.verno.lib.Lazy;
import ch.verno.lib.Publ;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.mail.MailLogBo;
import ch.verno.server.mail.factory.MailerFactory;
import jakarta.annotation.Nonnull;
import org.simplejavamail.api.email.Email;
import org.springframework.stereotype.Service;

@Service
public class MailSender {

  @Nonnull private final Lazy<MailLogBo> mailLogBo;
  @Nonnull private final Lazy<MailerFactory> mailerFactory;

  public MailSender(@Nonnull final ServerBean serverBean) {
    this.mailLogBo = Lazy.of(() -> BoFactory.getInstance(serverBean).get(MailLogBo.class));
    this.mailerFactory = Lazy.of(() -> serverBean.get(MailerFactory.class));
  }

  public void send(@Nonnull final Email email,
                   @Nonnull final MailConfigOptions.MailOrigin origin) {
    try (final var mailer = mailerFactory.get().create(origin)) {
      mailer.sendMail(email, true).get();

      mailLogBo.get().logSent(email, Publ.EMPTY_STRING);

    } catch (final Exception exception) {
      final var errorMessage = exception.getMessage() != null
              ? exception.getMessage()
              : "Unknown error";

      mailLogBo.get().logFailed(email, errorMessage);
      throw new SendMailException("Failed to send mail", exception);
    }

  }

}