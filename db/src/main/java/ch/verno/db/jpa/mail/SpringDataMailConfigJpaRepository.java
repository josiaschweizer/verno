package ch.verno.db.jpa.mail;

import ch.verno.db.entity.mail.MailConfigEntity;
import ch.verno.db.jpa.base.AbstractEntityJpaRepository;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public interface SpringDataMailConfigJpaRepository extends AbstractEntityJpaRepository<MailConfigEntity, Long> {

  @Nonnull
  Optional<MailConfigEntity> findByTenant_Id(@Nonnull Long tenantId);

  boolean existsByTenant_Id(@Nonnull Long tenantId);

}
