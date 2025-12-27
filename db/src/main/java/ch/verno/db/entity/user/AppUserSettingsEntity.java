package ch.verno.db.entity.user;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user_settings")
public class AppUserSettingsEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  protected AppUserSettingsEntity() {
    // JPA
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
