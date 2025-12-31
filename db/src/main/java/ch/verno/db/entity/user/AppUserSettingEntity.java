package ch.verno.db.entity.user;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user_settings")
public class AppUserSettingEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  private AppUserEntity user;

  private String theme;

  protected AppUserSettingEntity() {
    // JPA
  }

  public AppUserSettingEntity(final AppUserEntity user,
                              final String theme) {
    this.user = user;
    this.theme = theme;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public AppUserEntity getUser() {
    return user;
  }

  public void setUser(final AppUserEntity user) {
    this.user = user;
  }

  public String getTheme() {
    return theme;
  }

  public void setTheme(final String theme) {
    this.theme = theme;
  }
}
