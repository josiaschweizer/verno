package ch.verno.contract.dto.table.mail;

import ch.verno.common.type.mail.MailContentFormat;
import ch.verno.contract.dto.table.base.BaseDto;
import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class MailTemplateDto extends BaseDto<Long> {

  @Nonnull private String templateKey;
  @Nonnull private String subject;
  @Nonnull private String content;
  @Nonnull private MailContentFormat contentFormat;

  private MailTemplateDto() {
    setId(null);
    templateKey = Publ.EMPTY_STRING;
    subject = Publ.EMPTY_STRING;
    content = Publ.EMPTY_STRING;
    contentFormat = MailContentFormat.AUTO;
  }

  public MailTemplateDto(@Nonnull final String templateKey,
                         @Nonnull final String subject,
                         @Nonnull final String content,
                         @Nonnull final MailContentFormat contentFormat) {
    this(null, templateKey, subject, content, contentFormat);
  }

  public MailTemplateDto(@Nullable final Long id,
                         @Nonnull final String templateKey,
                         @Nonnull final String subject,
                         @Nonnull final String content,
                         @Nonnull final MailContentFormat contentFormat) {
    setId(id);
    setTenantId(id);
    this.templateKey = templateKey;
    this.subject = subject;
    this.content = content;
    this.contentFormat = contentFormat;
  }

  @Nonnull
  public static MailTemplateDto empty() {
    return new MailTemplateDto();
  }

  @Nonnull
  public static MailTemplateDto fromTemplateKey(@Nonnull final String templateKey) {
    final var dto = new MailTemplateDto();
    dto.setTemplateKey(templateKey);
    return dto;
  }

  @Nonnull
  public String getTemplateKey() {
    return templateKey;
  }

  public void setTemplateKey(@Nonnull final String templateKey) {
    this.templateKey = templateKey;
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
}