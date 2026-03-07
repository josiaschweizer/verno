package ch.verno.common.db.service.mail;

import ch.verno.common.db.dto.table.mail.MailConfigDto;
import ch.verno.common.db.enums.mail.MailValidity;
import jakarta.annotation.Nonnull;

public interface IMailConfigService {

  @Nonnull
  MailConfigDto upsertConfig(@Nonnull MailConfigDto dto);

  @Nonnull
  MailConfigDto getConfigForCurrentTenant();

  boolean hasConfigForCurrentTenant();

  void updateMailValidity(@Nonnull final MailValidity validity);
}
