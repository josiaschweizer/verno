package ch.verno.server.mapper.mail;

import ch.verno.contract.dto.table.mail.MailLogDto;
import ch.verno.db.entity.mail.MailLogEntity;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.server.mapper.base.IEntityMapper;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class MailLogMapper implements IEntityMapper<MailLogEntity, MailLogDto> {

  @Nonnull
  @Override
  public MailLogDto toSimpleDto(@Nonnull final MailLogEntity entity) {
    final var dto = MailLogDto.empty();

    dto.setId(entity.getId());
    dto.setTenantId(entity.getTenant() == null ? null : entity.getTenant().getId());

    dto.setRecipientEmail(entity.getRecipientEmail());
    dto.setRecipientName(entity.getRecipientName());
    dto.setTemplateName(entity.getTemplateName());
    dto.setSubject(entity.getSubject());
    dto.setContent(entity.getContent());
    dto.setPlaceholdersJson(entity.getPlaceholdersJson());
    dto.setStatus(entity.getStatus());
    dto.setErrorMessage(entity.getErrorMessage());
    dto.setProviderMessageId(entity.getProviderMessageId());
    dto.setSentAt(entity.getSentAt());
    dto.setCreatedAt(entity.getCreatedAt());
    dto.setCreatedBy(entity.getCreatedBy());

    return dto;
  }

  @Nonnull
  @Override
  public MailLogEntity toNewEntity(@Nonnull final MailLogDto dto) {
    final var entity = MailLogEntity.empty();
    updateEntity(entity, dto);

    if (dto.getTenantId() != null) {
      entity.setTenant(TenantEntity.ref(dto.getTenantId()));
    }

    return entity;
  }

  @Override
  public void updateEntity(@Nonnull final MailLogEntity entity,
                           @Nonnull final MailLogDto dto) {
    entity.setRecipientEmail(dto.getRecipientEmail());
    entity.setRecipientName(dto.getRecipientName());
    entity.setTemplateName(dto.getTemplateName());
    entity.setSubject(dto.getSubject());
    entity.setContent(dto.getContent());
    entity.setPlaceholdersJson(dto.getPlaceholdersJson());
    entity.setStatus(dto.getStatus());
    entity.setErrorMessage(dto.getErrorMessage());
    entity.setProviderMessageId(dto.getProviderMessageId());
    entity.setSentAt(dto.getSentAt());

    if (dto.getCreatedAt() != null) {
      entity.setCreatedAt(dto.getCreatedAt());
    }

    entity.setCreatedBy(dto.getCreatedBy());
  }
}