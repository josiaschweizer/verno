package ch.verno.server.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "course_level", schema = "public")
public class CourseLevelEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "code")
  private String code;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "sorting_order")
  private Integer sortingOrder;

  protected CourseLevelEntity() {
    // JPA
  }

  public CourseLevelEntity(@Nonnull final String code,
                           @Nonnull final String name,
                           final String description,
                           final Integer sortingOrder) {
    this.createdAt = Instant.now();
    this.code = code;
    this.name = name;
    this.description = description;
    this.sortingOrder = sortingOrder;
  }

  public Long getId() {
    return id;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nonnull final Instant createdAt) {
    this.createdAt = createdAt;
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