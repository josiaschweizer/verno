package ch.verno.server.bo.mail;

import ch.verno.common.tenant.TenantContext;
import ch.verno.common.type.mail.MailValidity;
import ch.verno.contract.dto.table.mail.MailConfigDto;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.mail.MailConfigMapper;
import ch.verno.server.service.intern.table.mail.MailConfigService;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class MailBo {

  @Nonnull private final MailConfigMapper mailConfigMapper;
  @Nonnull private final Lazy<MailConfigService> mailConfigService;

  public MailBo(@Nonnull final ServerBean serverBean) {
    this.mailConfigMapper = serverBean.get(MailConfigMapper.class);
    this.mailConfigService = Lazy.of(() -> serverBean.get(MailConfigService.class));
  }

  @Nonnull
  public Optional<MailConfigDto> getMailConfigForCurrentTenant() {
    final var currentTenant = TenantContext.getRequired();
    return mailConfigService.get().findByTenantId(currentTenant);
  }

  public boolean hasMailConfigForCurrentTenant() {
    final var currentTenant = TenantContext.getRequired();
    return mailConfigService.get().existsByTenantId(currentTenant);
  }

  @Nonnull
  public MailConfigDto updateCurrentMailValidity(@Nonnull final MailValidity mailValidity) {
    final var currentConfigOptional = getMailConfigForCurrentTenant();
    if (currentConfigOptional.isEmpty()) {
      return MailConfigDto.empty();
    }

    final var currentConfig = currentConfigOptional.get();
    currentConfig.setMailValidity(mailValidity);
    return mailConfigService.get().save(currentConfig);
  }

}
