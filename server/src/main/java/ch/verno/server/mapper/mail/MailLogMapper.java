package ch.verno.server.mapper.mail;

import ch.verno.common.db.dto.table.mail.MailLogDto;
import ch.verno.common.tenant.TenantContext;
import ch.verno.db.entity.mail.MailLogEntity;
import ch.verno.db.entity.tenant.TenantEntity;
import jakarta.annotation.Nonnull;

public final class MailLogMapper {

  @Nonnull
  public static MailLogDto toDto(@Nonnull final MailLogEntity entity) {
    return new MailLogDto(
            entity.getId(),
            entity.getTenant() != null ? entity.getTenant().getId() : null,
            entity.getRecipientEmail(),
            entity.getRecipientName(),
            entity.getTemplateName(),
            entity.getSubject(),
            entity.getContent(),
            entity.getPlaceholdersJson(),
            entity.getStatus(),
            entity.getErrorMessage(),
            entity.getProviderMessageId(),
            entity.getSentAt(),
            entity.getCreatedAt(),
            entity.getCreatedBy()
    );
  }

  @Nonnull
  public static MailLogEntity toEntity(@Nonnull final MailLogDto dto) {
    final var entity = new MailLogEntity();
    updateEntity(dto, entity);
    return entity;
  }

  public static void updateEntity(@Nonnull final MailLogDto dto,
                                  @Nonnull final MailLogEntity entity) {
    entity.setTenant(TenantContext.get() != null ?
            TenantEntity.ref(TenantContext.getRequired()) :
            null
    );
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
    entity.setCreatedAt(dto.getCreatedAt());
    entity.setCreatedBy(dto.getCreatedBy());
  }

}
