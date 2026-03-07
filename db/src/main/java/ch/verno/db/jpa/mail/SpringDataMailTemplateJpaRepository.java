package ch.verno.db.jpa.mail;

import ch.verno.db.entity.mail.MailTemplateEntity;
import ch.verno.db.entity.mail.MailTemplateId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataMailTemplateJpaRepository extends JpaRepository<MailTemplateEntity, MailTemplateId> {

  Optional<MailTemplateEntity> findByTenant_IdAndTemplateType_Key(Long tenantId, String templateKey);

  boolean existsByTenant_IdAndTemplateType_Key(Long tenantId, String templateKey);

  List<MailTemplateEntity> findAllByTenant_Id(Long tenantId);

  void deleteByTenant_IdAndTemplateType_Key(Long tenantId, String templateKey);

}
