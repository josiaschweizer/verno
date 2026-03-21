package ch.verno.common.db.dto.table.mail;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.db.type.mail.MailLogStatus;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

public class MailLogDto extends BaseDto {

  @Nonnull private String recipientEmail;
  @Nonnull private String recipientName;
  @Nonnull private String templateName;

  @Nonnull private String subject;
  @Nonnull private String content;

  @Nullable private String placeholdersJson;

  @Nonnull private MailLogStatus status;

  @Nonnull private String errorMessage;
  @Nonnull private String providerMessageId;

  private LocalDateTime sentAt;
  private LocalDateTime createdAt;
  private Long createdBy;

  public MailLogDto() {
    recipientEmail = Publ.EMPTY_STRING;
    recipientName = Publ.EMPTY_STRING;
    templateName = Publ.EMPTY_STRING;
    subject = Publ.EMPTY_STRING;
    content = Publ.EMPTY_STRING;
    placeholdersJson = Publ.EMPTY_STRING;
    errorMessage = Publ.EMPTY_STRING;
    providerMessageId = Publ.EMPTY_STRING;
    status = MailLogStatus.QUEUED;
  }

  public MailLogDto(@Nonnull final Long id,
                    @Nullable final Long tenantId,
                    @Nonnull final String recipientEmail,
                    @Nonnull final String recipientName,
                    @Nonnull final String templateName,
                    @Nonnull final String subject,
                    @Nonnull final String content,
                    @Nullable final String placeholdersJson,
                    @Nonnull final MailLogStatus status,
                    @Nonnull final String errorMessage,
                    @Nonnull final String providerMessageId,
                    final LocalDateTime sentAt,
                    final LocalDateTime createdAt,
                    final Long createdBy) {

    setId(id);
    setTenantId(tenantId);

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

  @Nonnull
  public String getRecipientEmail() {
    return recipientEmail;
  }

  public void setRecipientEmail(@Nonnull final String recipientEmail) {
    this.recipientEmail = recipientEmail;
  }

  @Nonnull
  public String getRecipientName() {
    return recipientName;
  }

  public void setRecipientName(@Nonnull final String recipientName) {
    this.recipientName = recipientName;
  }

  @Nonnull
  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(@Nonnull final String templateName) {
    this.templateName = templateName;
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

  @Nullable
  public String getPlaceholdersJson() {
    return placeholdersJson;
  }

  public void setPlaceholdersJson(@Nullable final String placeholdersJson) {
    this.placeholdersJson = placeholdersJson;
  }

  @Nonnull
  public MailLogStatus getStatus() {
    return status;
  }

  public void setStatus(@Nonnull final MailLogStatus status) {
    this.status = status;
  }

  @Nonnull
  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(@Nonnull final String errorMessage) {
    this.errorMessage = errorMessage;
  }

  @Nonnull
  public String getProviderMessageId() {
    return providerMessageId;
  }

  public void setProviderMessageId(@Nonnull final String providerMessageId) {
    this.providerMessageId = providerMessageId;
  }

  public LocalDateTime getSentAt() {
    return sentAt;
  }

  public void setSentAt(final LocalDateTime sentAt) {
    this.sentAt = sentAt;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(final LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Long getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(final Long createdBy) {
    this.createdBy = createdBy;
  }
}