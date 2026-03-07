package ch.verno.db.entity.mail;

import ch.verno.common.db.enums.mail.MailContentFormat;
import ch.verno.db.entity.tenant.TenantEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "mail_template", schema = "public")
public class MailTemplateEntity {

  @EmbeddedId
  private MailTemplateId id;

  @MapsId("tenantId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "tenant_id", nullable = false, updatable = false)
  private TenantEntity tenant;

  @MapsId("templateKey")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "template_key", nullable = false, updatable = false)
  private MailTemplateTypeEntity templateType;

  @Nonnull
  @Column(name = "subject", nullable = false)
  private String subject;

  @Nonnull
  @Column(name = "content", nullable = false, columnDefinition = "text")
  private String content;

  @Nonnull
  @Enumerated(EnumType.STRING)
  @Column(name = "content_format", nullable = false)
  private MailContentFormat contentFormat = MailContentFormat.AUTO;

  @Nonnull
  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @Nonnull
  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt = OffsetDateTime.now();

  protected MailTemplateEntity() {
  }

  public MailTemplateEntity(@Nonnull final TenantEntity tenant,
                            @Nonnull final MailTemplateTypeEntity templateType,
                            @Nonnull final String subject,
                            @Nonnull final String content,
                            @Nonnull final MailContentFormat contentFormat) {
    this.id = new MailTemplateId(tenant.getId(), templateType.getKey());
    this.tenant = tenant;
    this.templateType = templateType;
    this.subject = subject;
    this.content = content;
    this.contentFormat = contentFormat;
  }

  @PreUpdate
  protected void onPreUpdate() {
    updatedAt = OffsetDateTime.now();
  }

  public MailTemplateId getId() {
    return id;
  }

  public TenantEntity getTenant() {
    return tenant;
  }

  public void setTenant(final TenantEntity tenant) {
    this.tenant = tenant;
  }

  public MailTemplateTypeEntity getTemplateType() {
    return templateType;
  }

  public void setTemplateType(final MailTemplateTypeEntity templateType) {
    this.templateType = templateType;
  }

  @Nonnull
  public String getSubject() {
    return subject;
  }

  public void setSubject(@Nonnull final String subject) {
    this.subject = subject;
  }

  @Nonnull
  public String getContent() {
    return content;
  }

  public void setContent(@Nonnull final String content) {
    this.content = content;
  }

  @Nonnull
  public MailContentFormat getContentFormat() {
    return contentFormat;
  }

  public void setContentFormat(@Nonnull final MailContentFormat contentFormat) {
    this.contentFormat = contentFormat;
  }

  @Nonnull
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  @Nonnull
  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }
}