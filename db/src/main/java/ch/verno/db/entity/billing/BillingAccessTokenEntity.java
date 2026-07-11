package ch.verno.db.entity.billing;

import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.db.entity.tenant.TenantEntityListener;
import ch.verno.db.entity.tenant.TenantScopedEntity;
import ch.verno.db.entity.user.AppUserEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "billing_access_token", schema = "public")
@EntityListeners(TenantEntityListener.class)
public class BillingAccessTokenEntity extends TenantScopedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private AppUserEntity user;

  @Nonnull
  @Column(name = "token_hash", nullable = false, unique = true)
  private String tokenHash;

  @Nonnull
  @Column(name = "purpose", nullable = false)
  private String purpose;

  @Nonnull
  @Column(name = "expires_at", nullable = false)
  private OffsetDateTime expiresAt;

  @Column(name = "used_at")
  private OffsetDateTime usedAt;

  @Nonnull
  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  protected BillingAccessTokenEntity() {
    // JPA
  }

  public BillingAccessTokenEntity(@Nonnull final TenantEntity tenant,
                                  @Nonnull final AppUserEntity user,
                                  @Nonnull final String tokenHash,
                                  @Nonnull final String purpose,
                                  @Nonnull final OffsetDateTime expiresAt) {
    setTenant(tenant);
    this.user = user;
    this.tokenHash = tokenHash;
    this.purpose = purpose;
    this.expiresAt = expiresAt;
  }

  public BillingAccessTokenEntity(@Nonnull final AppUserEntity user,
                                  @Nonnull final String tokenHash,
                                  @Nonnull final String purpose,
                                  @Nonnull final OffsetDateTime expiresAt) {
    this.user = user;
    this.tokenHash = tokenHash;
    this.purpose = purpose;
    this.expiresAt = expiresAt;
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

  @Nonnull
  public String getTokenHash() {
    return tokenHash;
  }

  public void setTokenHash(@Nonnull final String tokenHash) {
    this.tokenHash = tokenHash;
  }

  @Nonnull
  public String getPurpose() {
    return purpose;
  }

  public void setPurpose(@Nonnull final String purpose) {
    this.purpose = purpose;
  }

  @Nonnull
  public OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nonnull final OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public OffsetDateTime getUsedAt() {
    return usedAt;
  }

  public void setUsedAt(final OffsetDateTime usedAt) {
    this.usedAt = usedAt;
  }

  @Nonnull
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nonnull final OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public boolean isUsed() {
    return usedAt != null;
  }

  public boolean isExpired() {
    return expiresAt.isBefore(OffsetDateTime.now());
  }
}