package ch.verno.db.entity.mandant;

import jakarta.persistence.*;

@Entity
@Table(name = "mandants")
public class MandantEntity {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  protected MandantEntity() {
    // JPA
  }

  public MandantEntity(final Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }
}