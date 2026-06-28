package ch.verno.server.mail.factory;

import ch.verno.common.type.mail.SmtpSecurity;
import ch.verno.contract.mail.MailConfigOptions;
import ch.verno.lib.Lazy;
import ch.verno.lib.VernoSecrets;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.env.EnvironmentVariableBo;
import ch.verno.server.mail.SmtpSecurityMapper;
import ch.verno.server.service.intern.table.mail.MailConfigService;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class MailConnectionConfigResolver {

  @Nonnull private final Lazy<EnvironmentVariableBo> envBo;
  @Nonnull private final Lazy<MailConfigService> mailConfigService;

  public MailConnectionConfigResolver(@Nonnull final ServerBean serverBean) {
    this.envBo = Lazy.of(() -> BoFactory.getInstance(serverBean).get(EnvironmentVariableBo.class));
    this.mailConfigService = Lazy.of(() -> serverBean.get(MailConfigService.class));
  }

  @Nonnull
  public MailConnectionConfig resolve(@Nonnull final MailConfigOptions.MailOrigin mailOrigin) {
    return switch (mailOrigin) {
      case TENANT_CONFIG -> resolveTenantConfig();
      case ENV -> resolveEnvironmentConfig();
    };
  }

  @Nonnull
  private MailConnectionConfig resolveTenantConfig() {
    final var config = mailConfigService.get().getRequiredMailConfigForCurrentTenant();

    return new MailConnectionConfig(
            config.getSmtpHost(),
            config.getSmtpPort(),
            config.getSmtpUsername(),
            config.getDecodedSmtpPassword(),
            SmtpSecurityMapper.toTransportStrategy(config.getSmtpSecurity())
    );
  }

  @Nonnull
  private MailConnectionConfig resolveEnvironmentConfig() {
    return new MailConnectionConfig(
            envBo.get().getEnv(VernoSecrets.SMTP_HOST),
            Integer.parseInt(envBo.get().getEnv(VernoSecrets.SMTP_PORT)),
            envBo.get().getEnv(VernoSecrets.SMTP_USER),
            envBo.get().getEnv(VernoSecrets.SMTP_PASS),
            SmtpSecurityMapper.toTransportStrategy(SmtpSecurity.fromString(envBo.get().getEnv(VernoSecrets.SMTP_SECURITY)))
    );
  }
}