package ch.verno.server.rpc.resource.mail;

import ch.verno.contract.dto.table.mail.MailTemplateDto;
import ch.verno.contract.endpoint.mail.MailTemplateResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.entity.mail.MailTemplateService;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RpcResource(MailTemplateResource.class)
public class MailTemplateResourceImpl implements MailTemplateResource {

  @Nonnull private final Lazy<MailTemplateService> mailTemplateService;

  public MailTemplateResourceImpl(@Nonnull final ServerBean serverBean) {
    this.mailTemplateService = Lazy.of(() -> serverBean.get(MailTemplateService.class));
  }

  @Override
  public boolean hasTemplateByKey(@Nonnull final String key) {
    return mailTemplateService.get().hasTemplateByKey(key);
  }

  @Nonnull
  @Override
  public Optional<MailTemplateDto> getTemplateByKey(@Nonnull final String key) {
    return mailTemplateService.get().findByKey(key);
  }

  @Nonnull
  @Override
  public MailTemplateDto saveMailTemplate(@Nonnull final MailTemplateDto mailTemplate) {
    return mailTemplateService.get().save(mailTemplate);
  }
}
