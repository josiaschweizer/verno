package ch.verno.rpc.client.mail;

import ch.verno.rpc.rpc.RpcFactory;
import ch.verno.contract.endpoint.mail.MailResource;
import ch.verno.lib.Lazy;
import jakarta.annotation.Nonnull;

public class MailClient {

  @Nonnull private final Lazy<MailResource> mailResource;

  public MailClient(@Nonnull final RpcFactory rpcFactory) {
    this.mailResource = Lazy.of(() -> rpcFactory.create(MailResource.class));
  }

}
