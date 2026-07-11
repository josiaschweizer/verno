package ch.verno.db.entity.mail;

import ch.verno.common.type.mail.MailValidity;
import ch.verno.common.type.mail.SmtpSecurity;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.lib.Publ;
import ch.verno.lib.VernoConstants;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "mail_config", schema = "public")
public class MailConfigEntity {

  @Id
  @Column(name = "tenant_id", nullable = false, updatable = false)
  private Long id;

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "tenant_id", nullable = false)
  private TenantEntity tenant;

  @Nonnull
  @Column(name = "from_name", nullable = false)
  private String fromName;

  @Nonnull
  @Column(name = "from_email", nullable = false)
  private String fromEmail;

  @Nullable
  @Column(name = "reply_to_email")
  private String replyToEmail;

  @Nullable
  @Column(name = "default_bcc")
  private String defaultBcc;

  @Nonnull
  @Column(name = "smtp_host", nullable = false)
  private String smtpHost;

  @Column(name = "smtp_port", nullable = false)
  private int smtpPort;

  @Nonnull
  @Column(name = "smtp_username", nullable = false)
  private String smtpUsername;

  @Nonnull
  @Column(name = "smtp_password_b64", nullable = false)
  private String smtpPasswordB64;

  @Nonnull
  @Enumerated(EnumType.STRING)
  @Column(name = "smtp_security", nullable = false)
  private SmtpSecurity smtpSecurity;

  @Column(name = "smtp_auth", nullable = false)
  private boolean smtpAuth = true;

  @Nonnull
  @Enumerated(EnumType.STRING)
  @Column(name = "mail_validity", nullable = false)
  private MailValidity mailValidity;

  @Nonnull
  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @Nonnull
  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt = OffsetDateTime.now();

  protected MailConfigEntity() {
    // JPA
  }

  public MailConfigEntity(@Nullable final TenantEntity tenant,
                          @Nonnull final String fromName,
                          @Nonnull final String fromEmail,
                          @Nullable final String replyToEmail,
                          @Nullable final String defaultBcc,
                          @Nonnull final String smtpHost,
                          final int smtpPort,
                          @Nonnull final String smtpUsername,
                          @Nonnull final String smtpPasswordB64,
                          @Nonnull final SmtpSecurity smtpSecurity,
                          final boolean smtpAuth,
                          @Nonnull final MailValidity mailValidity) {
    this.tenant = tenant;
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
    this.mailValidity = mailValidity;
  }

  @Nonnull
  public static MailConfigEntity ref(@Nonnull final Long id) {
    final var entity = new MailConfigEntity();
    entity.setId(id);
    return entity;
  }

  @Nonnull
  public static MailConfigEntity empty() {
    return new MailConfigEntity(
            null,
            Publ.EMPTY_STRING,
            Publ.EMPTY_STRING,
            Publ.EMPTY_STRING,
            Publ.EMPTY_STRING,
            Publ.EMPTY_STRING,
            VernoConstants.DEFAULT_SMTP_PORT,
            Publ.EMPTY_STRING,
            Publ.EMPTY_STRING,
            SmtpSecurity.NONE,
            false,
            MailValidity.UNTESTED
    );
  }

  @PreUpdate
  protected void onPreUpdate() {
    updatedAt = OffsetDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public void setId(@Nonnull final Long id) {
    this.id = id;
  }

  public TenantEntity getTenant() {
    return tenant;
  }

  public void setTenant(@Nonnull final TenantEntity tenant) {
    this.tenant = tenant;
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

  public void setSmtpPasswordB64(@Nonnull final String smtpPasswordB64) {
    this.smtpPasswordB64 = smtpPasswordB64;
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

  @Nonnull
  public MailValidity getMailValidity() {
    return mailValidity;
  }

  public void setMailValidity(@Nonnull final MailValidity mailValidity) {
    this.mailValidity = mailValidity;
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