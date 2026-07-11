package ch.verno.contract.endpoint.mail;

import ch.verno.common.type.mail.MailValidity;
import ch.verno.contract.dto.table.mail.MailConfigDto;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

import java.util.Optional;

@RpcEndpoint
public interface MailConfigResource {

  boolean hasMailConfigForCurrentTenant();

  @Nonnull
  Optional<MailConfigDto> getMailConfigForCurrentTenant();

  @Nonnull
  MailConfigDto saveMailConfig(@Nonnull MailConfigDto mailConfigDto);

  MailConfigDto updateCurrentMailValidity(@Nonnull MailValidity mailValidity);

}
