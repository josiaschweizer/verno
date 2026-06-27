package ch.verno.server.repository.mail;

import ch.verno.db.entity.mail.MailConfigEntity;
import ch.verno.db.jpa.mail.SpringDataMailConfigJpaRepository;
import ch.verno.server.repository.base.AbstractEntityRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MailConfigRepository extends AbstractEntityRepository<
        MailConfigEntity,
        Long,
        SpringDataMailConfigJpaRepository> {

  public MailConfigRepository(@Nonnull final SpringDataMailConfigJpaRepository repository) {
    super(repository);
  }

  @Nonnull
  public Optional<MailConfigEntity> findByTenantId(@Nonnull final Long tenantId) {
    return getRepository().findByTenant_Id(tenantId);
  }

  public boolean existsByTenantId(@Nonnull final Long tenantId) {
    return getRepository().existsByTenant_Id(tenantId);
  }
}
