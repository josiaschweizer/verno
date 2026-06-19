package ch.verno.rpc.client.mail;

import ch.verno.rpc.rpc.RpcFactory;
import ch.verno.contract.endpoint.mail.MailTemplateResource;
import ch.verno.lib.Lazy;
import jakarta.annotation.Nonnull;

public class MailTemplateClient {

  @Nonnull private final Lazy<MailTemplateResource> mailTemplateResource;

  public MailTemplateClient(@Nonnull final RpcFactory rpcFactory) {
    this.mailTemplateResource = Lazy.of(() -> rpcFactory.create(MailTemplateResource.class));
  }

}
