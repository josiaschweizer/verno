package ch.verno.server.mapper.db.mail;

import ch.verno.contract.dto.table.mail.MailTemplateDto;
import ch.verno.db.entity.mail.MailTemplateEntity;
import ch.verno.db.entity.mail.MailTemplateId;
import ch.verno.db.entity.mail.MailTemplateTypeEntity;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.server.mapper.db.base.IEntityMapper;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class MailTemplateMapper implements IEntityMapper<MailTemplateEntity, MailTemplateDto> {

  @Nonnull
  @Override
  public MailTemplateDto toSimpleDto(@Nonnull final MailTemplateEntity entity) {
    final var dto = MailTemplateDto.empty();

    final var id = entity.getId();

    if (id != null) {
      dto.setId(id.getTenantId());
      dto.setTenantId(id.getTenantId());
      dto.setTemplateKey(id.getTemplateKey());
    } else if (entity.getTemplateType() != null) {
      dto.setTemplateKey(entity.getTemplateType().getKey());
    }

    dto.setSubject(entity.getSubject());
    dto.setContent(entity.getContent());
    dto.setContentFormat(entity.getContentFormat());

    return dto;
  }

  @Nonnull
  @Override
  public MailTemplateEntity toNewEntity(@Nonnull final MailTemplateDto dto) {
    final var entity = MailTemplateEntity.empty();
    updateEntity(entity, dto);

    if (dto.getTenantId() != null) {
      entity.setTenant(TenantEntity.ref(dto.getTenantId()));
      entity.setTemplateType(MailTemplateTypeEntity.ref(dto.getTemplateKey()));
      entity.setId(MailTemplateId.of(dto.getTenantId(), dto.getTemplateKey()));
    }

    return entity;
  }

  @Override
  public void updateEntity(@Nonnull final MailTemplateEntity entity,
                           @Nonnull final MailTemplateDto dto) {
    entity.setSubject(dto.getSubject());
    entity.setContent(dto.getContent());
    entity.setContentFormat(dto.getContentFormat());

    if (!dto.getTemplateKey().isBlank()) {
      entity.setTemplateType(MailTemplateTypeEntity.ref(dto.getTemplateKey()));

      if (dto.getTenantId() != null) {
        entity.setId(MailTemplateId.of(dto.getTenantId(), dto.getTemplateKey()));
      }
    }
  }
}