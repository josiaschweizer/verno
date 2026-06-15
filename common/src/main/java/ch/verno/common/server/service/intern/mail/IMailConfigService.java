package ch.verno.common.server.service.intern.mail;

import ch.verno.common.db.dto.table.mail.MailConfigDto;
import ch.verno.common.db.type.mail.MailValidity;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public interface IMailConfigService {

  @Nonnull
  MailConfigDto upsertConfig(@Nonnull MailConfigDto dto);

  @Nonnull
  MailConfigDto getConfigForCurrentTenant();

  @Nonnull
  Optional<MailConfigDto> getOptionalConfigForCurrentTenant();

  boolean hasConfigForCurrentTenant();

  void updateMailValidity(@Nonnull final MailValidity validity);
}
