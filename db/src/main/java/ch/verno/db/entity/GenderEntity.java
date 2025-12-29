package ch.verno.db.entity;

import ch.verno.common.util.Publ;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "gender", schema = "public")
public class GenderEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  protected GenderEntity() {
    // JPA
  }

  public GenderEntity(@Nonnull final String name,
                      @Nonnull final String description) {
    this.createdAt = OffsetDateTime.now();
    this.name = name;
    this.description = description;
  }

  @Nonnull
  public static GenderEntity ref(@Nonnull final Long id) {
    final var entity = new GenderEntity();
    entity.setId(id);
    return entity;
  }

  public static GenderEntity empty() {
    return new GenderEntity(Publ.EMPTY_STRING, Publ.EMPTY_STRING);
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(final OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }
}
