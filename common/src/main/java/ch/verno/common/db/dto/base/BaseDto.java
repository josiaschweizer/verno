package ch.verno.common.db.dto.base;

import jakarta.annotation.Nullable;

public class BaseDto {

  @Nullable
  private Long id;

  @Nullable
  private Long tenantId; // new: tenant id shared by all DTOs

  @Nullable
  public Long getId() {
    return id;
  }

  public void setId(@Nullable final Long id) {
    this.id = id;
  }

  @Nullable
  public Long getTenantId() {
    return tenantId;
  }

  public void setTenantId(@Nullable final Long tenantId) {
    this.tenantId = tenantId;
  }
}
