package ch.verno.common.db.dto.table.mail;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.db.enums.mail.MailContentFormat;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;

public class MailTemplateDto extends BaseDto {

  @Nonnull private String templateKey;
  @Nonnull private String subject;
  @Nonnull private String content;
  @Nonnull private MailContentFormat contentFormat;

  public MailTemplateDto() {
    templateKey = Publ.EMPTY_STRING;
    subject = Publ.EMPTY_STRING;
    content = Publ.EMPTY_STRING;
    contentFormat = MailContentFormat.AUTO;
  }

  public MailTemplateDto(@Nonnull final Long id,
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