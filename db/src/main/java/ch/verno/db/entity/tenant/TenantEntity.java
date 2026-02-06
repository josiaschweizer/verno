package ch.verno.db.entity.tenant;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mandants")
public class TenantEntity {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @Column(name = "slug", nullable = false, unique = true, length = 64)
  private String slug;

  @Column(name = "name", length = 128)
  private String name;

  protected TenantEntity() {
    // JPA
  }

  private TenantEntity(@Nonnull final Long id) {
    this.id = id;
  }

  public static TenantEntity ref(@Nonnull final Long id) {
    return new TenantEntity(id);
  }

  @Nonnull
  public Long getId() {
    return id;
  }

  public void setId(@Nonnull final Long id) {
    this.id = id;
  }

  @Nonnull
  public String getSlug() {
    return slug;
  }

  public void setSlug(@Nonnull final String slug) {
    this.slug = slug;
  }

  @Nullable
  public String getName() {
    return name;
  }

  public void setName(@Nullable final String name) {
    this.name = name;
  }
}