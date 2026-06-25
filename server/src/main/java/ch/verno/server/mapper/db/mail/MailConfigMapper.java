package ch.verno.server.mapper.db.mail;

import ch.verno.contract.dto.table.mail.MailConfigDto;
import ch.verno.db.entity.mail.MailConfigEntity;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.server.mapper.db.base.IEntityMapper;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class MailConfigMapper implements IEntityMapper<MailConfigEntity, MailConfigDto> {

  @Nonnull
  @Override
  public MailConfigDto toSimpleDto(@Nonnull final MailConfigEntity entity) {
    final var dto = MailConfigDto.empty();

    dto.setId(entity.getId());
    dto.setTenantId(entity.getId());

    dto.setFromName(entity.getFromName());
    dto.setFromEmail(entity.getFromEmail());
    dto.setReplyToEmail(entity.getReplyToEmail());
    dto.setDefaultBcc(entity.getDefaultBcc());

    dto.setSmtpHost(entity.getSmtpHost());
    dto.setSmtpPort(entity.getSmtpPort());
    dto.setSmtpUsername(entity.getSmtpUsername());
    dto.setSmtpPasswordB64(entity.getSmtpPasswordB64());
    dto.setSmtpSecurity(entity.getSmtpSecurity());
    dto.setSmtpAuth(entity.isSmtpAuth());
    dto.setMailValidity(entity.getMailValidity());

    return dto;
  }

  @Nonnull
  @Override
  public MailConfigEntity toNewEntity(@Nonnull final MailConfigDto dto) {
    final var entity = MailConfigEntity.empty();
    updateEntity(entity, dto);

    if (dto.getTenantId() != null) {
      entity.setTenant(TenantEntity.ref(dto.getTenantId()));
      entity.setId(dto.getTenantId());
    }

    return entity;
  }

  @Override
  public void updateEntity(@Nonnull final MailConfigEntity entity,
                           @Nonnull final MailConfigDto dto) {
    entity.setFromName(dto.getFromName());
    entity.setFromEmail(dto.getFromEmail());
    entity.setReplyToEmail(dto.getReplyToEmail());
    entity.setDefaultBcc(dto.getDefaultBcc());

    entity.setSmtpHost(dto.getSmtpHost());
    entity.setSmtpPort(dto.getSmtpPort());
    entity.setSmtpUsername(dto.getSmtpUsername());
    entity.setSmtpPasswordB64(dto.getSmtpPasswordB64());
    entity.setSmtpSecurity(dto.getSmtpSecurity());
    entity.setSmtpAuth(dto.isSmtpAuth());
    entity.setMailValidity(dto.getMailValidity());
  }
}