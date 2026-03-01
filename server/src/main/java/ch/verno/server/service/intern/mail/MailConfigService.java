package ch.verno.server.service.intern.mail;

import ch.verno.common.db.dto.table.mail.MailConfigDto;
import ch.verno.common.db.service.mail.IMailConfigService;
import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.common.tenant.TenantContext;
import ch.verno.db.entity.mail.MailConfigEntity;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.server.mapper.mail.MailConfigMapper;
import ch.verno.server.repository.mail.MailConfigRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MailConfigService implements IMailConfigService {

  @Nonnull private final MailConfigRepository mailConfigRepository;

  public MailConfigService(@Nonnull final MailConfigRepository mailConfigRepository) {
    this.mailConfigRepository = mailConfigRepository;
  }

  @Nonnull
  @Override
  @Transactional
  public MailConfigDto upsertConfig(@Nonnull final MailConfigDto dto) {
    final var tenantId = TenantContext.getRequired();

    final var existing = mailConfigRepository.findByTenantId(tenantId).orElse(null);

    final MailConfigEntity entity;
    if (existing == null) {
      entity = new MailConfigEntity(
              TenantEntity.ref(tenantId),
              dto.isEnabled(),
              dto.getFromName(),
              dto.getFromEmail(),
              dto.getReplyToEmail(),
              dto.getDefaultBcc(),
              dto.getSmtpHost(),
              dto.getSmtpPort(),
              dto.getSmtpUsername(),
              dto.getSmtpPasswordB64(),
              dto.getSmtpSecurity(),
              dto.isSmtpAuth()
      );
    } else {
      entity = existing;
      MailConfigMapper.updateEntity(entity, dto);
    }

    final var saved = mailConfigRepository.save(entity);
    return MailConfigMapper.toDto(saved);
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public MailConfigDto getConfigForCurrentTenant() {
    final var tenantId = TenantContext.getRequired();

    final var entity = mailConfigRepository.findByTenantId(tenantId)
            .orElseThrow(() -> new DBNotFoundException(
                    DBNotFoundReason.MAIL_CONFIG_BY_TENANT_NOT_FOUND,
                    tenantId
            ));

    return MailConfigMapper.toDto(entity);
  }

  @Override
  public boolean hasConfigForCurrentTenant(){
    final var tenantId = TenantContext.getRequired();
    return mailConfigRepository.findByTenantId(tenantId).isPresent();
  }

  @Override
  @Transactional
  public void setEnabled(final boolean enabled) {
    final var tenantId = TenantContext.getRequired();

    final var entity = mailConfigRepository.findByTenantId(tenantId)
            .orElseThrow(() -> new DBNotFoundException(
                    DBNotFoundReason.MAIL_CONFIG_BY_TENANT_NOT_FOUND,
                    tenantId
            ));

    entity.setEnabled(enabled);
    mailConfigRepository.save(entity);
  }
}