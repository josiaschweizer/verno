package ch.verno.db.jpa.mail;

import ch.verno.common.type.mail.MailLogStatus;
import ch.verno.db.entity.mail.MailLogEntity;
import ch.verno.db.jpa.base.AbstractEntityJpaRepository;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface SpringDataMailLogJpaRepository extends AbstractEntityJpaRepository<MailLogEntity, Long> {

  @Nonnull
  List<MailLogEntity> findAllByStatusOrderByCreatedAtDesc(@Nonnull MailLogStatus status);

  @Nonnull
  List<MailLogEntity> findAllByRecipientEmailOrderByCreatedAtDesc(@Nonnull String recipientEmail);

}
