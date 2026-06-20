package ch.verno.server.repository.mail;

import ch.verno.common.type.mail.MailLogStatus;
import ch.verno.db.entity.mail.MailLogEntity;
import ch.verno.db.jpa.mail.SpringDataMailLogJpaRepository;
import ch.verno.server.repository.base.AbstractEntityRepository;
import jakarta.annotation.Nonnull;

import java.util.List;

public class MailLogRepository extends AbstractEntityRepository<
        MailLogEntity,
        Long,
        SpringDataMailLogJpaRepository> {

  public MailLogRepository(@Nonnull final SpringDataMailLogJpaRepository repository) {
    super(repository);
  }

  @Nonnull
  public List<MailLogEntity> findAllByStatus(@Nonnull final MailLogStatus status) {
    return getRepository().findAllByStatusOrderByCreatedAtDesc(status);
  }

  @Nonnull
  public List<MailLogEntity> findAllByRecipientEmail(@Nonnull final String recipientEmail) {
    return getRepository().findAllByRecipientEmailOrderByCreatedAtDesc(recipientEmail);
  }



}
