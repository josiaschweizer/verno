package ch.verno.server.mail.mailer;

import ch.verno.contract.mail.MailConfigOptions;
import ch.verno.lib.Publ;
import ch.verno.lib.VernoSecrets;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.env.EnvironmentVariableBo;
import ch.verno.server.mail.SmtpSecurityMapper;
import ch.verno.server.service.intern.table.mail.MailConfigService;
import jakarta.annotation.Nonnull;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.mailer.MailerBuilder;

public class MailerUtil {

  @Nonnull
  public static Mailer createMailer(@Nonnull final ServerBean serverBean,
                                    @Nonnull final MailConfigOptions.MailOrigin mailOrigin) {
    String smtpHost;
    int smtpPort;
    String smtpUser;
    String smtpPassword;
    TransportStrategy transportationStrategy;

    if (mailOrigin == MailConfigOptions.MailOrigin.TENANT_CONFIG) {
      final var mailConfigService = serverBean.get(MailConfigService.class);
      final var mailConfig = mailConfigService.getRequiredMailConfigForCurrentTenant();

      smtpHost = mailConfig.getSmtpHost();
      smtpPort = mailConfig.getSmtpPort();
      smtpUser = mailConfig.getSmtpUsername();
      smtpPassword = mailConfig.getDecodedSmtpPassword();
      transportationStrategy = SmtpSecurityMapper.toTransportStrategy(mailConfig.getSmtpSecurity());
    } else if (mailOrigin == MailConfigOptions.MailOrigin.ENV) {
      final var envBo = serverBean.get(EnvironmentVariableBo.class);

      smtpHost = envBo.getEnv(VernoSecrets.SMTP_HOST);
      smtpPort = Integer.parseInt(envBo.getEnv(VernoSecrets.SMTP_PORT));
      smtpUser = envBo.getEnv(VernoSecrets.SMTP_USER);
      smtpPassword = envBo.getEnv(VernoSecrets.SMTP_PASS);
      transportationStrategy = SmtpSecurityMapper.toTransportStrategy(envBo.getEnv(VernoSecrets.SMTP_SECURITY));
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

}
