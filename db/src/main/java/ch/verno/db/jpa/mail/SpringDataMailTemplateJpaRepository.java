package ch.verno.db.jpa.mail;

import ch.verno.db.entity.mail.MailTemplateEntity;
import ch.verno.db.entity.mail.MailTemplateId;
import ch.verno.db.jpa.base.AbstractEntityJpaRepository;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public interface SpringDataMailTemplateJpaRepository extends AbstractEntityJpaRepository<MailTemplateEntity, MailTemplateId> {

  @Nonnull
  Optional<MailTemplateEntity> findTemplateType_Key(@Nonnull String templateKey); //TODO vorher mit findByTenant_IdAndTemplateType_Key mit tenantid gewesen

  boolean existsTemplateType_Key(@Nonnull String templateKey);

  List<MailTemplateEntity> findAllByTenant_Id(Long tenantId);

  void deleteByTenant_IdAndTemplateType_Key(Long tenantId, String templateKey);

}
