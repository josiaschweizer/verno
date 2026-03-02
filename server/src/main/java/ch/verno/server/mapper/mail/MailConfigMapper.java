package ch.verno.server.mapper.mail;

import ch.verno.common.db.dto.table.mail.MailConfigDto;
import ch.verno.db.entity.mail.MailConfigEntity;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public final class MailConfigMapper {

  private MailConfigMapper() {
  }

  @Nonnull
  public static MailConfigDto toDto(@Nonnull final MailConfigEntity entity) {
    return new MailConfigDto(
            entity.getTenant().getId(),
            entity.getFromName(),
            entity.getFromEmail(),
            Optional.ofNullable(entity.getReplyToEmail()).orElse(Publ.EMPTY_STRING),
            Optional.ofNullable(entity.getDefaultBcc()).orElse(Publ.EMPTY_STRING),
            entity.getSmtpHost(),
            entity.getSmtpPort(),
            entity.getSmtpUsername(),
            entity.getSmtpPasswordB64(),
            entity.getSmtpSecurity(),
            entity.isSmtpAuth(),
            entity.getMailValidity()
    );
  }

  @Nonnull
  public static MailConfigEntity toEntity(@Nonnull final MailConfigDto dto,
                                          @Nonnull final TenantEntity tenant) {
    return new MailConfigEntity(
            tenant,
            dto.getFromName(),
            dto.getFromEmail(),
            dto.getReplyToEmail(),
            dto.getDefaultBcc(),
            dto.getSmtpHost(),
            dto.getSmtpPort(),
            dto.getSmtpUsername(),
            dto.getSmtpPasswordB64(),
            dto.getSmtpSecurity(),
            dto.isSmtpAuth(),
            dto.getMailValidity()
    );
  }

  public static void updateEntity(@Nonnull final MailConfigEntity entity,
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