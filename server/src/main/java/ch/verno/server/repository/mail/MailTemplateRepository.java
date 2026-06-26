package ch.verno.server.repository.mail;

import ch.verno.db.entity.mail.MailTemplateEntity;
import ch.verno.db.entity.mail.MailTemplateId;
import ch.verno.db.jpa.mail.SpringDataMailTemplateJpaRepository;
import ch.verno.server.repository.base.AbstractEntityRepository;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class MailTemplateRepository extends AbstractEntityRepository<
        MailTemplateEntity,
        MailTemplateId,
        SpringDataMailTemplateJpaRepository> {

  public MailTemplateRepository(@Nonnull final SpringDataMailTemplateJpaRepository repository) {
    super(repository);
  }

  public boolean hasByKey(@Nonnull final String key) {
    return getRepository().existsTemplateType_Key(key);
  }

  @Nonnull
  public Optional<MailTemplateEntity> findByKey(@Nonnull final String key) {
    return getRepository().findTemplateType_Key(key);
  }

}
