package ch.verno.server.service.intern.mail;

import ch.verno.common.db.dto.table.mail.MailTemplateDto;
import ch.verno.common.db.service.mail.IMailTemplateService;
import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.common.tenant.TenantContext;
import ch.verno.db.entity.mail.MailTemplateEntity;
import ch.verno.db.entity.mail.MailTemplateTypeEntity;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.server.mapper.mail.MailTemplateMapper;
import ch.verno.server.repository.mail.MailTemplateRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MailTemplateService implements IMailTemplateService {

  @Nonnull private final MailTemplateRepository mailTemplateRepository;

  public MailTemplateService(@Nonnull final MailTemplateRepository mailTemplateRepository) {
    this.mailTemplateRepository = mailTemplateRepository;
  }

  @Nonnull
  @Override
  @Transactional
  public MailTemplateDto upsertTemplate(@Nonnull final MailTemplateDto dto) {
    final var tenantId = TenantContext.getRequired();

    final var existing = mailTemplateRepository
            .findByTenantAndTemplateKey(tenantId, dto.getTemplateKey())
            .orElse(null);

    final MailTemplateEntity entity;
    if (existing == null) {
      entity = new MailTemplateEntity(
              TenantEntity.ref(tenantId),
              MailTemplateTypeEntity.ref(dto.getTemplateKey()),
              dto.getSubject(),
              dto.getContent(),
              dto.getContentFormat()
      );
    } else {
      entity = existing;
      MailTemplateMapper.updateEntity(entity, dto);
    }

    final var saved = mailTemplateRepository.save(entity);
    return MailTemplateMapper.toDto(saved);
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public MailTemplateDto getTemplateByKey(@Nonnull final String templateKey) {
    final var tenantId = TenantContext.getRequired();

    final var entity = mailTemplateRepository
            .findByTenantAndTemplateKey(tenantId, templateKey)
            .orElseThrow(() -> new DBNotFoundException(
                    DBNotFoundReason.MAIL_TEMPLATE_BY_KEY_NOT_FOUND,
                    templateKey
            ));

    return MailTemplateMapper.toDto(entity);
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<MailTemplateDto> getAllTemplatesForCurrentTenant() {
    final var tenantId = TenantContext.getRequired();

    return mailTemplateRepository.findAllByTenant(tenantId).stream()
            .map(MailTemplateMapper::toDto)
            .toList();
  }

  @Override
  @Transactional
  public void deleteTemplate(@Nonnull final String templateKey) {
    final var tenantId = TenantContext.getRequired();
    mailTemplateRepository.deleteByTenantAndTemplateKey(tenantId, templateKey);
  }
}