package ch.verno.contract.endpoint.mail;

import ch.verno.contract.dto.table.mail.MailConfigDto;
import ch.verno.contract.dto.table.mail.MailLogDto;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public interface MailConfigResource {

  boolean hasMailConfigForCurrentTenant();

  @Nonnull
  Optional<MailConfigDto> getMailConfigForCurrentTenant();

}
