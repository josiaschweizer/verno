package ch.verno.server.repository.mail;

import ch.verno.common.tenant.TenantContext;
import ch.verno.db.entity.mail.MailTemplateEntity;
import ch.verno.db.entity.mail.MailTemplateId;
import ch.verno.db.jpa.mail.SpringDataMailTemplateJpaRepository;
import ch.verno.server.repository.base.AbstractEntityRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MailTemplateRepository extends AbstractEntityRepository<
        MailTemplateEntity,
        MailTemplateId,
        SpringDataMailTemplateJpaRepository> {

  public MailTemplateRepository(@Nonnull final SpringDataMailTemplateJpaRepository repository) {
    super(repository);
  }

  public boolean hasByKey(@Nonnull final String key) {
    return getRepository().existsByTenant_IdAndTemplateType_Key(TenantContext.get(), key); //TODO tenant context
  }

  @Nonnull
  public Optional<MailTemplateEntity> findByKey(@Nonnull final String key) {
    return getRepository().findByTenant_IdAndTemplateType_Key(TenantContext.get(), key); //TODO tenant context
  }

}
