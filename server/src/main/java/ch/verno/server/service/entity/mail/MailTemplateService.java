package ch.verno.server.service.entity.mail;

import ch.verno.common.tenant.TenantContext;
import ch.verno.contract.dto.table.mail.MailTemplateDto;
import ch.verno.db.entity.mail.MailTemplateEntity;
import ch.verno.db.entity.mail.MailTemplateId;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.mail.MailTemplateMapper;
import ch.verno.server.repository.mail.MailTemplateRepository;
import ch.verno.server.service.base.AbstractEntityServiceUndefinedId;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MailTemplateService extends AbstractEntityServiceUndefinedId<
        MailTemplateEntity,
        MailTemplateDto,
        MailTemplateRepository,
        MailTemplateMapper> {

  public MailTemplateService(@Nonnull final ServerBean serverBean) {
    super(serverBean.get(MailTemplateRepository.class), serverBean.get(MailTemplateMapper.class));
  }

  public boolean hasTemplateByKey(@Nonnull final String key) {
    return getRepository().hasByKey(key);
  }

  @Nonnull
  public Optional<MailTemplateDto> findByKey(@Nonnull final String key) {
    return getRepository().findByKey(key).map(getMapper()::toSimpleDto);
  }

  @Nonnull
  @Override
  protected MailTemplateDto update(@Nonnull final MailTemplateDto dto) {
    return null;
  }

  public void deleteById(@Nonnull final String key) {
    final var currentTenant = TenantContext.getRequired(); //TODO refactor to not have to use tenant context here
    getRepository().deleteById(MailTemplateId.of(currentTenant, key));
  }

}
