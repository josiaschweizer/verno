package ch.verno.server.bo.mail;

import ch.verno.common.tenant.TenantContext;
import ch.verno.contract.dto.table.mail.MailConfigDto;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.db.mail.MailConfigMapper;
import ch.verno.server.repository.mail.MailConfigRepository;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class MailBo {

  @Nonnull private final MailConfigMapper mailConfigMapper;
  @Nonnull private final Lazy<MailConfigRepository> mailConfigRepository;

  protected MailBo(@Nonnull final ServerBean serverBean) {
    this.mailConfigMapper = serverBean.get(MailConfigMapper.class);
    this.mailConfigRepository = Lazy.of(() -> serverBean.get(MailConfigRepository.class));
  }

  @Nonnull
  public Optional<MailConfigDto> getMailConfigForCurrentTenant() {
    final var currentTenant = TenantContext.getRequired();
    return mailConfigRepository.get().findByTenantId(currentTenant)
            .map(mailConfigMapper::toSimpleDto);
  }

  public boolean hasMailConfigForCurrentTenant() {
    final var currentTenant = TenantContext.getRequired();
    return mailConfigRepository.get().findByTenantId(currentTenant).isPresent();
  }

}
