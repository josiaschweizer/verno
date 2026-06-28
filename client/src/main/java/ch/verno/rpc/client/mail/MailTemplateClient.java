package ch.verno.rpc.client.mail;

import ch.verno.contract.dto.table.mail.MailTemplateDto;
import ch.verno.contract.endpoint.mail.MailTemplateResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class MailTemplateClient {

  @Nonnull private final Lazy<MailTemplateResource> mailTemplateResource;

  @Inject
  public MailTemplateClient(@Nonnull final RpcFactory rpcFactory) {
    this.mailTemplateResource = Lazy.of(() -> rpcFactory.create(MailTemplateResource.class));
  }

  public boolean hasTemplateByKey(@Nonnull final String key) {
    return mailTemplateResource.get().hasTemplateByKey(key);
  }

  @Nonnull
  public Optional<MailTemplateDto> getTemplateByKey(@Nonnull final String key) {
    return mailTemplateResource.get().getTemplateByKey(key);
  }

  @Nonnull
  public MailTemplateDto saveMailTemplate(@Nonnull final  MailTemplateDto mailTemplate) {
    return mailTemplateResource.get().saveMailTemplate(mailTemplate);
  }

}
