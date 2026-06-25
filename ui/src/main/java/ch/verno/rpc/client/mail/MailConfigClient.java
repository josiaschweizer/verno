package ch.verno.rpc.client.mail;

import ch.verno.contract.dto.table.mail.MailConfigDto;
import ch.verno.contract.endpoint.mail.MailConfigResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class MailConfigClient {

  @Nonnull private final Lazy<MailConfigResource> mailConfigResource;

  public MailConfigClient(@Nonnull final RpcFactory rpcFactory){
    this.mailConfigResource = Lazy.of(() -> rpcFactory.create(MailConfigResource.class));
  }

  public boolean hasMailConfigForCurrentTenant() {
    return mailConfigResource.get().hasMailConfigForCurrentTenant();
  }

  @Nonnull
  public Optional<MailConfigDto> getMailConfigForCurrentTenant() {
    return mailConfigResource.get().getMailConfigForCurrentTenant();
  }

}
