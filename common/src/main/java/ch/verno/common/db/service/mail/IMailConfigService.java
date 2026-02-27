package ch.verno.common.db.service.mail;

import ch.verno.common.db.dto.table.mail.MailConfigDto;
import jakarta.annotation.Nonnull;

public interface IMailConfigService {

  @Nonnull
  MailConfigDto upsertConfig(@Nonnull MailConfigDto dto);

  @Nonnull
  MailConfigDto getConfigForCurrentTenant();

  void setEnabled(boolean enabled);
}
