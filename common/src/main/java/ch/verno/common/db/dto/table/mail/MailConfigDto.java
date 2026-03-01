package ch.verno.common.db.dto.table.mail;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.db.enums.mail.SmtpSecurity;
import ch.verno.common.lib.Base64Util;
import ch.verno.publ.Publ;
import ch.verno.publ.VernoConstants;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class MailConfigDto extends BaseDto {

  private boolean enabled;

  @Nonnull private String fromName;
  @Nonnull private String fromEmail;
  @Nullable private String replyToEmail;
  @Nullable private String defaultBcc;

  @Nonnull private String smtpHost;
  private int smtpPort;
  @Nonnull private String smtpUsername;
  @Nonnull private String smtpPasswordB64;
  @Nonnull private SmtpSecurity smtpSecurity;
  private boolean smtpAuth;

  public MailConfigDto() {
    enabled = true;
    fromName = Publ.EMPTY_STRING;
    fromEmail = Publ.EMPTY_STRING;
    replyToEmail = Publ.EMPTY_STRING;
    defaultBcc = Publ.EMPTY_STRING;
    smtpHost = Publ.EMPTY_STRING;
    smtpPort = VernoConstants.DEFAULT_SMTP_PORT;
    smtpUsername = Publ.EMPTY_STRING;
    smtpPasswordB64 = Publ.EMPTY_STRING;
    smtpSecurity = SmtpSecurity.STARTTLS;
    smtpAuth = true;
  }

  public MailConfigDto(@Nonnull final Long id,
                       final boolean enabled,
                       @Nonnull final String fromName,
                       @Nonnull final String fromEmail,
                       @Nonnull final String replyToEmail,
                       @Nonnull final String defaultBcc,
                       @Nonnull final String smtpHost,
                       final int smtpPort,
                       @Nonnull final String smtpUsername,
                       @Nonnull final String smtpPasswordB64,
                       @Nonnull final SmtpSecurity smtpSecurity,
                       final boolean smtpAuth) {
    setId(id);
    setTenantId(id);
    this.enabled = enabled;
    this.fromName = fromName;
    this.fromEmail = fromEmail;
    this.replyToEmail = replyToEmail;
    this.defaultBcc = defaultBcc;
    this.smtpHost = smtpHost;
    this.smtpPort = smtpPort;
    this.smtpUsername = smtpUsername;
    this.smtpPasswordB64 = smtpPasswordB64;
    this.smtpSecurity = smtpSecurity;
    this.smtpAuth = smtpAuth;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

  @Nonnull
  public String getFromName() {
    return fromName;
  }

  public void setFromName(@Nonnull final String fromName) {
    this.fromName = fromName;
  }

  @Nonnull
  public String getFromEmail() {
    return fromEmail;
  }

  public void setFromEmail(@Nonnull final String fromEmail) {
    this.fromEmail = fromEmail;
  }

  @Nullable
  public String getReplyToEmail() {
    return replyToEmail;
  }

  public void setReplyToEmail(@Nullable final String replyToEmail) {
    this.replyToEmail = replyToEmail;
  }

  @Nullable
  public String getDefaultBcc() {
    return defaultBcc;
  }

  public void setDefaultBcc(@Nullable final String defaultBcc) {
    this.defaultBcc = defaultBcc;
  }

  @Nonnull
  public String getSmtpHost() {
    return smtpHost;
  }

  public void setSmtpHost(@Nonnull final String smtpHost) {
    this.smtpHost = smtpHost;
  }

  public int getSmtpPort() {
    return smtpPort;
  }

  public void setSmtpPort(final int smtpPort) {
    this.smtpPort = smtpPort;
  }

  @Nonnull
  public String getSmtpUsername() {
    return smtpUsername;
  }

  public void setSmtpUsername(@Nonnull final String smtpUsername) {
    this.smtpUsername = smtpUsername;
  }

  @Nonnull
  public String getSmtpPasswordB64() {
    return smtpPasswordB64;
  }

  @Nonnull
  public String getDecodedSmtpPassword() {
    return Base64Util.decodeToString(smtpPasswordB64);
  }

  public void setSmtpPasswordB64(@Nonnull final String smtpPasswordB64) {
    this.smtpPasswordB64 = smtpPasswordB64;
  }

  public void setDecodedPasswordB64(@Nonnull final String decodedPassword) {
    this.smtpPasswordB64 = Base64Util.encodeString(decodedPassword);
  }

  @Nonnull
  public SmtpSecurity getSmtpSecurity() {
    return smtpSecurity;
  }

  public void setSmtpSecurity(@Nonnull final SmtpSecurity smtpSecurity) {
    this.smtpSecurity = smtpSecurity;
  }

  public boolean isSmtpAuth() {
    return smtpAuth;
  }

  public void setSmtpAuth(final boolean smtpAuth) {
    this.smtpAuth = smtpAuth;
  }
}