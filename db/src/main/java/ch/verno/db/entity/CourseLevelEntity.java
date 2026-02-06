package ch.verno.db.entity;

import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.db.entity.tenant.TenantEntityListener;
import ch.verno.db.entity.tenant.TenantScopedEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "course_level",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_course_level_mandant_code",
                        columnNames = {"mandant_id", "code"}
                )
        }
)
@EntityListeners(TenantEntityListener.class)
public class CourseLevelEntity extends TenantScopedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  @Column(name = "code", nullable = false, length = 64)
  private String code;

  @Column(name = "name", nullable = false, length = 128)
  private String name;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "sorting_order")
  private Integer sortingOrder;

  protected CourseLevelEntity() {
    // JPA
  }

  public CourseLevelEntity(@Nonnull final TenantEntity tenant,
                           @Nonnull final String code,
                           @Nonnull final String name,
                           final String description,
                           final Integer sortingOrder) {
    setTenant(tenant);
    this.code = code;
    this.name = name;
    this.description = description;
    this.sortingOrder = sortingOrder;
  }

  @Nonnull
  public static CourseLevelEntity ref(@Nonnull final Long id) {
    final var entity = new CourseLevelEntity();
    entity.setId(id);
    return entity;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public String getCode() {
    return code;
  }

  public void setCode(final String code) {
    this.code = code;
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

  public Integer getSortingOrder() {
    return sortingOrder;
  }

  public void setSortingOrder(final Integer sortingOrder) {
    this.sortingOrder = sortingOrder;
  }
}