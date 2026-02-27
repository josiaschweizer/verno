package ch.verno.db.jpa.mail;

import ch.verno.db.entity.mail.MailConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataMailConfigJpaRepository extends JpaRepository<MailConfigEntity, Long> {

  Optional<MailConfigEntity> findByTenant_Id(Long tenantId);

  boolean existsByTenant_Id(Long tenantId);

}
