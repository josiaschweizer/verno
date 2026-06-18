package ch.verno.db.entity.mail;

import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mail_template_type", schema = "public")
public class MailTemplateTypeEntity {

  @Id
  @Nonnull
  @Column(name = "key", nullable = false, updatable = false)
  private String key;

  protected MailTemplateTypeEntity() {
    // JPA
  }

  public MailTemplateTypeEntity(@Nonnull final String key) {
    this.key = key;
  }

  @Nonnull
  public static MailTemplateTypeEntity empty() {
    return new MailTemplateTypeEntity(Publ.EMPTY_STRING);
  }

  @Nonnull
  public static MailTemplateTypeEntity ref(@Nonnull final String key) {
    return new MailTemplateTypeEntity(key);
  }

  @Nonnull
  public String getKey() {
    return key;
  }
}