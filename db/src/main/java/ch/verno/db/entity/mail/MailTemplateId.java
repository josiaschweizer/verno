package ch.verno.db.entity.mail;

import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MailTemplateId implements Serializable {

  @Column(name = "tenant_id", nullable = false)
  private Long tenantId;

  @Column(name = "template_key", nullable = false)
  private String templateKey;

  protected MailTemplateId() {
    // JPA
  }

  public MailTemplateId(@Nonnull final Long tenantId,
                        @Nonnull final String templateKey) {
    this.tenantId = tenantId;
    this.templateKey = templateKey;
  }

  @Nonnull
  public static MailTemplateId empty() {
    return new MailTemplateId(Publ.ZERO_LONG, Publ.EMPTY_STRING);
  }

  @Nonnull
  public static MailTemplateId of(@Nonnull final Long tenantId,
                                  @Nonnull final String templateKey) {
    return new MailTemplateId(tenantId, templateKey);
  }

  public Long getTenantId() {
    return tenantId;
  }

  public String getTemplateKey() {
    return templateKey;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof MailTemplateId that)) return false;
    return Objects.equals(tenantId, that.tenantId)
            && Objects.equals(templateKey, that.templateKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tenantId, templateKey);
  }
}