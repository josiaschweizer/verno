package ch.verno.server.repository.mail;

import ch.verno.db.entity.mail.MailTemplateEntity;
import ch.verno.db.jpa.mail.SpringDataMailTemplateJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MailTemplateRepository {

  @Nonnull private final SpringDataMailTemplateJpaRepository jpaRepository;

  public MailTemplateRepository(@Nonnull final SpringDataMailTemplateJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Nonnull
  public Optional<MailTemplateEntity> findByTenantAndTemplateKey(@Nonnull final Long tenantId,
                                                                 @Nonnull final String templateKey) {
    return jpaRepository.findByTenant_IdAndTemplateType_Key(tenantId, templateKey);
  }

  public boolean existsByTenantAndTemplateKey(@Nonnull final Long tenantId,
                                              @Nonnull final String templateKey) {
    return jpaRepository.existsByTenant_IdAndTemplateType_Key(tenantId, templateKey);
  }

  @Nonnull
  public List<MailTemplateEntity> findAllByTenant(@Nonnull final Long tenantId) {
    return jpaRepository.findAllByTenant_Id(tenantId);
  }

  @Nonnull
  public MailTemplateEntity save(@Nonnull final MailTemplateEntity template) {
    return jpaRepository.save(template);
  }

  public void deleteByTenantAndTemplateKey(@Nonnull final Long tenantId,
                                           @Nonnull final String templateKey) {
    jpaRepository.deleteByTenant_IdAndTemplateType_Key(tenantId, templateKey);
  }

  public void flush() {
    jpaRepository.flush();
  }
}
