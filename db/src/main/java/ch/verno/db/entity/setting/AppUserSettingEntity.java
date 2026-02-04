package ch.verno.db.entity.setting;

import ch.verno.db.entity.mandant.MandantEntity;
import ch.verno.db.entity.mandant.MandantEntityListener;
import ch.verno.db.entity.mandant.MandantScopedEntity;
import ch.verno.db.entity.user.AppUserEntity;
import jakarta.persistence.*;

@Entity
@Table(
        name = "app_user_settings",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_settings_user", columnNames = {"user_id"})
        }
)
@EntityListeners(MandantEntityListener.class)
public class AppUserSettingEntity extends MandantScopedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private AppUserEntity user;

  @Column(nullable = false, length = 16)
  private String theme;

  @Column(name = "language_tag", nullable = false, length = 16)
  private String languageTag;

  protected AppUserSettingEntity() {
    // JPA
  }

  public AppUserSettingEntity(final MandantEntity mandant,
                              final AppUserEntity user,
                              final String theme,
                              final String languageTag) {
    setMandant(mandant);
    this.user = user;
    this.theme = theme;
    this.languageTag = languageTag;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
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