package ch.verno.db.entity.mail;

import ch.verno.common.db.enums.mail.MailLogStatus;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.db.entity.tenant.TenantEntityListener;
import ch.verno.db.entity.tenant.TenantScopedEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mail_log")
@EntityListeners(TenantEntityListener.class)
public class MailLogEntity extends TenantScopedEntity {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 320)
  private String recipientEmail;

  @Column(length = 200)
  private String recipientName;

  @Column(length = 200)
  private String templateName;

  @Column(nullable = false, length = 500)
  private String subject;

  @Column(nullable = false, columnDefinition = "text")
  private String content;

  @Column(columnDefinition = "text")
  private String placeholdersJson;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private MailLogStatus status;

  @Column(columnDefinition = "text")
  private String errorMessage;

  @Column(length = 200)
  private String providerMessageId;

  @Column
  private LocalDateTime sentAt;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column
  private Long createdBy;

  public MailLogEntity() {
  }

  public MailLogEntity(@Nonnull final TenantEntity tenant,
                       @Nonnull final String recipientEmail,
                       @Nonnull final String recipientName,
                       @Nonnull final String templateName,
                       @Nonnull final String subject,
                       @Nonnull final String content,
                       @Nonnull final String placeholdersJson,
                       @Nonnull final MailLogStatus status,
                       @Nonnull final String errorMessage,
                       @Nonnull final String providerMessageId,
                       final LocalDateTime sentAt,
                       @Nonnull final LocalDateTime createdAt,
                       final Long createdBy) {

    setTenant(tenant);
    this.recipientEmail = recipientEmail;
    this.recipientName = recipientName;
    this.templateName = templateName;
    this.subject = subject;
    this.content = content;
    this.placeholdersJson = placeholdersJson;
    this.status = status;
    this.errorMessage = errorMessage;
    this.providerMessageId = providerMessageId;
    this.sentAt = sentAt;
    this.createdAt = createdAt;
    this.createdBy = createdBy;
  }

  public Long getId() {
    return id;
  }

  public void setId(@Nonnull Long id) {
    this.id = id;
  }

  public String getRecipientEmail() {
    return recipientEmail;
  }

  public void setRecipientEmail(@Nonnull String recipientEmail) {
    this.recipientEmail = recipientEmail;
  }

  public String getRecipientName() {
    return recipientName;
  }

  public void setRecipientName(String recipientName) {
    this.recipientName = recipientName;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(@Nonnull String subject) {
    this.subject = subject;
  }

  public String getContent() {
    return content;
  }

  public void setContent(@Nonnull String content) {
    this.content = content;
  }

  public String getPlaceholdersJson() {
    return placeholdersJson;
  }

  public void setPlaceholdersJson(String placeholdersJson) {
    this.placeholdersJson = placeholdersJson;
  }

  public MailLogStatus getStatus() {
    return status;
  }

  public void setStatus(@Nonnull MailLogStatus status) {
    this.status = status;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getProviderMessageId() {
    return providerMessageId;
  }

  public void setProviderMessageId(String providerMessageId) {
    this.providerMessageId = providerMessageId;
  }

  public LocalDateTime getSentAt() {
    return sentAt;
  }

  public void setSentAt(LocalDateTime sentAt) {
    this.sentAt = sentAt;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nonnull LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Long getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Long createdBy) {
    this.createdBy = createdBy;
  }
}