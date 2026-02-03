package ch.verno.db.entity.mandant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mandants")
public class MandantEntity {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @Column(name = "slug", nullable = false, unique = true, length = 64)
  private String slug;

  protected MandantEntity() {
    // JPA
  }

  private MandantEntity(final Long id) {
    this.id = id;
  }

  public static MandantEntity ref(final long id) {
    return new MandantEntity(id);
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(final String slug) {
    this.slug = slug;
  }
}