package ch.verno.server.repository.mail;

import ch.verno.db.entity.mail.MailConfigEntity;
import ch.verno.db.jpa.mail.SpringDataMailConfigJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MailConfigRepository {

  @Nonnull private final SpringDataMailConfigJpaRepository jpaRepository;

  public MailConfigRepository(@Nonnull final SpringDataMailConfigJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Nonnull
  public Optional<MailConfigEntity> findByTenantId(@Nonnull final Long tenantId) {
    return jpaRepository.findByTenant_Id(tenantId);
  }

  public boolean existsByTenantId(@Nonnull final Long tenantId) {
    return jpaRepository.existsByTenant_Id(tenantId);
  }

  @Nonnull
  public MailConfigEntity save(@Nonnull final MailConfigEntity config) {
    return jpaRepository.save(config);
  }

  public void flush() {
    jpaRepository.flush();
  }
}
