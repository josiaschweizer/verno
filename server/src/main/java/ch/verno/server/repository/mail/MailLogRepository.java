package ch.verno.server.repository.mail;

import ch.verno.common.db.enums.mail.MailLogStatus;
import ch.verno.db.entity.mail.MailLogEntity;
import ch.verno.db.jpa.mail.SpringDataMailLogJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MailLogRepository {

  @Nonnull
  private final SpringDataMailLogJpaRepository repository;

  public MailLogRepository(@Nonnull final SpringDataMailLogJpaRepository repository) {
    this.repository = repository;
  }

  @Nonnull
  public MailLogEntity save(@Nonnull final MailLogEntity entity) {
    return repository.save(entity);
  }

  @Nonnull
  public Optional<MailLogEntity> findById(@Nonnull final Long id) {
    return repository.findById(id);
  }

  @Nonnull
  public List<MailLogEntity> findAll() {
    return repository.findAll();
  }

  @Nonnull
  public List<MailLogEntity> findAllByStatusOrderByCreatedAtDesc(@Nonnull final MailLogStatus status) {
    return repository.findAllByStatusOrderByCreatedAtDesc(status);
  }

  @Nonnull
  public List<MailLogEntity> findAllByRecipientEmailOrderByCreatedAtDesc(@Nonnull final String recipientEmail) {
    return repository.findAllByRecipientEmailOrderByCreatedAtDesc(recipientEmail);
  }

  @Nonnull
  public List<MailLogEntity> findTop100ByOrderByCreatedAtDesc() {
    return repository.findTop100ByOrderByCreatedAtDesc();
  }

  public void deleteById(@Nonnull final Long id) {
    repository.deleteById(id);
  }

  public boolean existsById(@Nonnull final Long id) {
    return repository.existsById(id);
  }
}