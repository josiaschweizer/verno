package ch.verno.db.jpa.mail;

import ch.verno.common.type.mail.MailLogStatus;
import ch.verno.db.entity.mail.MailLogEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SpringDataMailLogJpaRepository extends
        JpaRepository<MailLogEntity, Long>,
        JpaSpecificationExecutor<MailLogEntity> {

  @Nonnull
  List<MailLogEntity> findAllByStatusOrderByCreatedAtDesc(@Nonnull MailLogStatus status);

  @Nonnull
  List<MailLogEntity> findAllByRecipientEmailOrderByCreatedAtDesc(@Nonnull String recipientEmail);

  @Nonnull
  List<MailLogEntity> findTop100ByOrderByCreatedAtDesc();

}
