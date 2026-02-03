package ch.verno.db.entity;

import ch.verno.db.entity.mandant.MandantEntity;
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
public class CourseLevelEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "mandant_id", nullable = false)
  private MandantEntity mandant;

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

  public CourseLevelEntity(@Nonnull final MandantEntity mandant,
                           @Nonnull final String code,
                           @Nonnull final String name,
                           final String description,
                           final Integer sortingOrder) {
    this.mandant = mandant;
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

  public MandantEntity getMandant() {
    return mandant;
  }

  public void setMandant(final MandantEntity mandant) {
    this.mandant = mandant;
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