package ch.verno.contract.endpoint.mail;

import ch.verno.contract.dto.table.mail.MailConfigDto;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public interface MailConfigResource {

  boolean hasMailConfigForCurrentTenant();

  @Nonnull
  Optional<MailConfigDto> getMailConfigForCurrentTenant();

}
