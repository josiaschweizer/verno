package ch.verno.contract.endpoint.mail;

import ch.verno.contract.dto.table.mail.MailTemplateDto;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

import java.util.Optional;

@RpcEndpoint
public interface MailTemplateResource {

  boolean hasTemplateByKey(@Nonnull String key);

  @Nonnull
  Optional<MailTemplateDto> getTemplateByKey(@Nonnull String key);

  @Nonnull
  MailTemplateDto saveMailTemplate(@Nonnull MailTemplateDto mailTemplate);

}
