package ch.verno.db.jpa.mail;

import ch.verno.db.entity.mail.MailConfigEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataMailConfigJpaRepository extends
        JpaRepository<MailConfigEntity, Long> {

  @Nonnull
  Optional<MailConfigEntity> findByTenant_Id(@Nonnull Long tenantId);

  boolean existsByTenant_Id(@Nonnull Long tenantId);

}
