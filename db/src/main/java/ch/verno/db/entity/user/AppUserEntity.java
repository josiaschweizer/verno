package ch.verno.db.entity.user;

import ch.verno.db.entity.mandant.MandantEntity;
import ch.verno.db.entity.mandant.MandantEntityListener;
import ch.verno.db.entity.mandant.MandantScopedEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

@Entity
@Table(
        name = "app_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_app_user_mandant_username", columnNames = {"mandant_id", "username"})
        }
)
@EntityListeners(MandantEntityListener.class)
public class AppUserEntity extends MandantScopedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 200)
  private String username;

  @Column(nullable = false, length = 100)
  private String passwordHash;

  @Column(nullable = false, length = 64)
  private String role;

  protected AppUserEntity() {
    // JPA
  }

  public AppUserEntity(@Nonnull final MandantEntity mandant,
                       @Nonnull final String username,
                       @Nonnull final String passwordHash,
                       @Nonnull final String role) {
    setMandant(mandant);
    this.username = username;
    this.passwordHash = passwordHash;
    this.role = role;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(final String username) {
    this.username = username;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(final String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public String getRole() {
    return role;
  }

  public void setRole(final String role) {
    this.role = role;
  }
}