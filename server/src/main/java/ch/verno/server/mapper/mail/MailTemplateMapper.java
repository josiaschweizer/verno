package ch.verno.server.mapper.mail;

import ch.verno.common.db.dto.table.mail.MailTemplateDto;
import ch.verno.db.entity.mail.MailTemplateEntity;
import ch.verno.db.entity.mail.MailTemplateTypeEntity;
import ch.verno.db.entity.tenant.TenantEntity;
import jakarta.annotation.Nonnull;

public final class MailTemplateMapper {

  private MailTemplateMapper() {
  }

  @Nonnull
  public static MailTemplateDto toDto(@Nonnull final MailTemplateEntity entity) {
    return new MailTemplateDto(
            entity.getTenant().getId(),
            entity.getTemplateType().getKey(),
            entity.getSubject(),
            entity.getContent(),
            entity.getContentFormat()
    );
  }

  @Nonnull
  public static MailTemplateEntity toEntity(@Nonnull final MailTemplateDto dto,
                                            @Nonnull final TenantEntity tenant,
                                            @Nonnull final MailTemplateTypeEntity templateType) {
    return new MailTemplateEntity(
            tenant,
            templateType,
            dto.getSubject(),
            dto.getContent(),
            dto.getContentFormat()
    );
  }

  public static void updateEntity(@Nonnull final MailTemplateEntity entity,
                                  @Nonnull final MailTemplateDto dto) {
    entity.setSubject(dto.getSubject());
    entity.setContent(dto.getContent());
    entity.setContentFormat(dto.getContentFormat());
  }
}