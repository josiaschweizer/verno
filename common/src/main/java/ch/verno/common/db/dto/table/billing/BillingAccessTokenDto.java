package ch.verno.common.db.dto.table.billing;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.OffsetDateTime;
import java.util.Objects;

public class BillingAccessTokenDto extends BaseDto {

  @Nullable
  private Long userId;

  @Nonnull
  private String tokenHash;

  @Nonnull
  private String purpose;

  @Nullable
  private OffsetDateTime expiresAt;

  @Nullable
  private OffsetDateTime usedAt;

  @Nullable
  private OffsetDateTime createdAt;

  public BillingAccessTokenDto() {
    setId(null);
    this.userId = null;
    this.tokenHash = Publ.EMPTY_STRING;
    this.purpose = Publ.EMPTY_STRING;
    this.expiresAt = null;
    this.usedAt = null;
    this.createdAt = null;
  }

  public BillingAccessTokenDto(@Nullable final Long id,
                               @Nullable final Long userId,
                               @Nonnull final String tokenHash,
                               @Nonnull final String purpose,
                               @Nullable final OffsetDateTime expiresAt,
                               @Nullable final OffsetDateTime usedAt,
                               @Nullable final OffsetDateTime createdAt) {
    setId(id);
    this.userId = userId;
    this.tokenHash = tokenHash;
    this.purpose = purpose;
    this.expiresAt = expiresAt;
    this.usedAt = usedAt;
    this.createdAt = createdAt;
  }

  @Nullable
  public Long getUserId() {
    return userId;
  }

  public void setUserId(@Nullable final Long userId) {
    this.userId = userId;
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

  @Nullable
  public OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable final OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  @Nullable
  public OffsetDateTime getUsedAt() {
    return usedAt;
  }

  public void setUsedAt(@Nullable final OffsetDateTime usedAt) {
    this.usedAt = usedAt;
  }

  @Nullable
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable final OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public boolean isUsed() {
    return usedAt != null;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof BillingAccessTokenDto other)) return false;
    return getId() != null && getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}