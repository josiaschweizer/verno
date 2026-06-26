package ch.verno.server.rpc.resource.mail;

import ch.verno.contract.dto.table.mail.MailConfigDto;
import ch.verno.contract.endpoint.mail.MailConfigResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.intern.table.mail.MailConfigService;
import jakarta.annotation.Nonnull;

import java.util.Optional;

@SuppressWarnings("unused")
@RpcResource(MailConfigResource.class)
public class MailConfigResourceImpl implements MailConfigResource {

  @Nonnull private final Lazy<MailConfigService> mailConfigService;

  public MailConfigResourceImpl(@Nonnull final ServerBean serverBean) {
    this.mailConfigService = Lazy.of(() -> serverBean.get(MailConfigService.class));
  }

  @Override
  public boolean hasMailConfigForCurrentTenant(){
    return mailConfigService.get().hasMailConfigForCurrentTenant();
  }

  @Nonnull
  @Override
  public Optional<MailConfigDto> getMailConfigForCurrentTenant() {
    return mailConfigService.get().getMailConfigForCurrentTenant();
  }

}
