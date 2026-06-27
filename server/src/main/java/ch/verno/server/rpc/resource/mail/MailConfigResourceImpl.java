package ch.verno.server.rpc.resource.mail;

import ch.verno.common.type.mail.MailValidity;
import ch.verno.contract.dto.table.mail.MailConfigDto;
import ch.verno.contract.endpoint.mail.MailConfigResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.mail.MailBo;
import ch.verno.server.service.intern.table.mail.MailConfigService;
import jakarta.annotation.Nonnull;

import java.util.Optional;

@SuppressWarnings("unused")
@RpcResource(MailConfigResource.class)
public class MailConfigResourceImpl implements MailConfigResource {

  @Nonnull private final Lazy<MailBo> mailBo;
  @Nonnull private final Lazy<MailConfigService> mailConfigService;

  public MailConfigResourceImpl(@Nonnull final ServerBean serverBean) {
    this.mailBo = Lazy.of(() -> serverBean.get(BoFactory.class).get(MailBo.class));
    this.mailConfigService = Lazy.of(() -> serverBean.get(MailConfigService.class));
  }

  @Override
  public boolean hasMailConfigForCurrentTenant(){
    return mailBo.get().hasMailConfigForCurrentTenant();
  }

  @Nonnull
  @Override
  public Optional<MailConfigDto> getMailConfigForCurrentTenant() {
    return mailBo.get().getMailConfigForCurrentTenant();
  }

  @Nonnull
  @Override
  public MailConfigDto saveMailConfig(@Nonnull final MailConfigDto mailConfigDto) {
    return mailConfigService.get().save(mailConfigDto);
  }

  @Nonnull
  @Override
  public MailConfigDto updateCurrentMailValidity(@Nonnull final MailValidity mailValidity) {
    return mailBo.get().updateCurrentMailValidity(mailValidity);
  }
}
