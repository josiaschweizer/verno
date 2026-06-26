package ch.verno.contract.dto.table.base;

import jakarta.annotation.Nullable;

public class BaseDto<ID> {

  @Nullable private ID id;
  @Nullable private Long tenantId; // new: tenant id shared by all DTOs

  @Nullable
  public ID getId() {
    return id;
  }

  public void setId(@Nullable final ID id) {
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
