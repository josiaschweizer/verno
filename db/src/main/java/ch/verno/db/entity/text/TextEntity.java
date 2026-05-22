package ch.verno.db.entity.text;

import ch.verno.db.entity.tenant.TenantEntityListener;
import ch.verno.db.entity.tenant.TenantScopedEntity;
import ch.verno.lib.Language;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "text", schema = "public")
@EntityListeners(TenantEntityListener.class)
public class TextEntity extends TenantScopedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Nonnull
  @Column(name = "identifier", nullable = false)
  private String identifier;

  @Nonnull
  @Column(name = "language_code", nullable = false)
  private String languageCode;

  @Nonnull
  @Column(name = "text_value", nullable = false)
  private String text;

  @Nonnull
  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @Nonnull
  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt = OffsetDateTime.now();

  protected TextEntity() {
    // JPA
  }

  public TextEntity(@Nonnull String identifier,
                    @Nonnull String languageCode,
                    @Nonnull String text) {
    this.identifier = identifier;
    this.languageCode = languageCode;
    this.text = text;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Nonnull
  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(@Nonnull final String identifier) {
    this.identifier = identifier;
  }

  @Nonnull
  public String getLanguageCode() {
    return languageCode;
  }

  public void setLanguageCode(@Nonnull final String languageCode) {
    this.languageCode = languageCode;
  }

  @Nonnull
  public Language getLanguage() {
    return Language.fromCode(languageCode);
  }

  @Nonnull
  public String getText() {
    return text;
  }

  public void setText(@Nonnull final String text) {
    this.text = text;
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
