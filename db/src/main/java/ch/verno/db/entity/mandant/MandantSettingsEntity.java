package ch.verno.db.entity.mandant;

import jakarta.persistence.*;

@Entity
@Table(name = "mandant_settings")
public class MandantSettingsEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  protected MandantSettingsEntity() {
    // JPA
  }

  private Long getId() {
    return id;
  }

  private void setId(final Long id) {
    this.id = id;
  }
}
