package ch.verno.server.service.intern.table.mail;

import ch.verno.contract.dto.table.mail.MailConfigDto;
import ch.verno.db.entity.mail.MailConfigEntity;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.mail.MailBo;
import ch.verno.server.mapper.db.mail.MailConfigMapper;
import ch.verno.server.repository.mail.MailConfigRepository;
import ch.verno.server.service.base.AbstractEntityService;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class MailConfigService extends AbstractEntityService<
        MailConfigEntity,
        MailConfigDto,
        MailConfigRepository,
        MailConfigMapper> {

  @Nonnull private final Lazy<MailBo> mailBo;

  public MailConfigService(@Nonnull final ServerBean serverBean) {
    super(serverBean.get(MailConfigRepository.class), serverBean.get(MailConfigMapper.class));
    this.mailBo = Lazy.of(() -> serverBean.get(MailBo.class));
  }

  @Nonnull
  public Optional<MailConfigDto> findByTenantId(@Nonnull final Long tenantId) {
    return getRepository().findByTenantId(tenantId)
            .map(getMapper()::toSimpleDto);
  }

  public boolean existsByTenantId(@Nonnull final Long tenantId) {
    return getRepository().existsByTenantId(tenantId);
  }

  public boolean hasMailConfigForCurrentTenant() {
    return mailBo.get().hasMailConfigForCurrentTenant();
  }

  @Nonnull
  public Optional<MailConfigDto> getMailConfigForCurrentTenant() {
    return mailBo.get().getMailConfigForCurrentTenant();
  }

  @Nonnull
  public MailConfigDto getRequiredMailConfigForCurrentTenant() {
    final var config = getMailConfigForCurrentTenant();
    if (config.isEmpty()) {
      throw new IllegalStateException("Mail config for Tenant " + config.get().getTenantId() + " is empty");
    }

    return config.get();
  }

}
