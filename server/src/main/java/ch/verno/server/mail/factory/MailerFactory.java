package ch.verno.server.mail.factory;

import ch.verno.contract.mail.MailConfigOptions;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import jakarta.annotation.Nonnull;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.stereotype.Component;

@Component
public class MailerFactory {

  @Nonnull private final Lazy<MailConnectionConfigResolver> configResolver;

  public MailerFactory(@Nonnull final ServerBean serverBean) {
    this.configResolver = Lazy.of(() -> serverBean.get(MailConnectionConfigResolver.class));
  }

  @Nonnull
  public Mailer create(@Nonnull final MailConfigOptions.MailOrigin origin) {
    final var config = configResolver.get().resolve(origin);

    return MailerBuilder
            .withSMTPServer(
                    config.smtpHost(),
                    config.smtpPort(),
                    config.smtpUsername(),
                    config.smtpPassword()
            )
            .withTransportStrategy(config.transportStrategy())
            .withSessionTimeout(5000)
            .buildMailer();
  }
}