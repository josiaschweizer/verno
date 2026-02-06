package ch.verno.db.entity.user;

import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.db.entity.tenant.TenantEntityListener;
import ch.verno.db.entity.tenant.TenantScopedEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

@Entity
@Table(
        name = "app_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_app_user_mandant_username", columnNames = {"mandant_id", "username"})
        }
)
@EntityListeners(TenantEntityListener.class)
public class AppUserEntity extends TenantScopedEntity {

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

  public AppUserEntity(@Nonnull final TenantEntity tenant,
                       @Nonnull final String username,
                       @Nonnull final String passwordHash,
                       @Nonnull final String role) {
    setTenant(tenant);
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