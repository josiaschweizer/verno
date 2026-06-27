package ch.verno.db.jpa.mail;

import ch.verno.db.entity.mail.MailTemplateEntity;
import ch.verno.db.entity.mail.MailTemplateId;
import ch.verno.db.jpa.base.AbstractEntityJpaRepository;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public interface SpringDataMailTemplateJpaRepository extends AbstractEntityJpaRepository<MailTemplateEntity, MailTemplateId> {

  boolean existsByTenant_IdAndTemplateType_Key(@Nonnull Long tenantId,
                                               @Nonnull String templateKey);

  @Nonnull
  Optional<MailTemplateEntity> findByTenant_IdAndTemplateType_Key(@Nonnull Long tenantId,
                                                                  @Nonnull String templateKey); //TODO vorher mit findByTenant_IdAndTemplateType_Key mit tenantid gewesen

  List<MailTemplateEntity> findAllByTenant_Id(Long tenantId);

  void deleteByTenant_IdAndTemplateType_Key(Long tenantId, String templateKey);

}
