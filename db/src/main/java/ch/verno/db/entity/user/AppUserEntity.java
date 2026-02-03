package ch.verno.db.entity.user;

import ch.verno.db.entity.mandant.MandantEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

@Entity
@Table(
        name = "app_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_app_user_mandant_username", columnNames = {"mandant_id", "username"})
        }
)
public class AppUserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "mandant_id", nullable = false)
  private MandantEntity mandant;

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
    this.mandant = mandant;
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

  public MandantEntity getMandant() {
    return mandant;
  }

  public void setMandant(final MandantEntity mandant) {
    this.mandant = mandant;
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