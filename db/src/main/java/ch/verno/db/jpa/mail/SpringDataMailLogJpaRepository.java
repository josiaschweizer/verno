package ch.verno.db.jpa.mail;

import ch.verno.common.db.enums.mail.MailLogStatus;
import ch.verno.db.entity.mail.MailLogEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataMailLogJpaRepository extends JpaRepository<MailLogEntity, Long> {

  @Nonnull
  List<MailLogEntity> findAllByStatusOrderByCreatedAtDesc(@Nonnull MailLogStatus status);

  @Nonnull
  List<MailLogEntity> findAllByRecipientEmailOrderByCreatedAtDesc(@Nonnull String recipientEmail);

  @Nonnull
  List<MailLogEntity> findTop100ByOrderByCreatedAtDesc();

}
