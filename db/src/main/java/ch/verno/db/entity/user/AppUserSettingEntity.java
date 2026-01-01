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

  @Column(nullable = false, length = 16)
  private String theme;

  @Column(name = "language_tag", nullable = false, length = 16)
  private String languageTag;

  protected AppUserSettingEntity() {
    // JPA
  }

  public AppUserSettingEntity(final AppUserEntity user,
                              final String theme,
                              final String languageTag) {
    this.user = user;
    this.theme = theme;
    this.languageTag = languageTag;
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

  public String getLanguageTag() {
    return languageTag;
  }

  public void setLanguageTag(final String languageTag) {
    this.languageTag = languageTag;
  }
}