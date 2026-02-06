package ch.verno.common.db.dto.base;

import jakarta.annotation.Nullable;

public class BaseDto {

  @Nullable
  private Long id;

  @Nullable
  private Long tenant; // new: tenant/mandant id shared by all DTOs

  @Nullable
  public Long getId() {
    return id;
  }

  public void setId(@Nullable final Long id) {
    this.id = id;
  }

  @Nullable
  public Long getTenant() {
    return tenant;
  }

  public void setTenant(@Nullable final Long tenant) {
    this.tenant = tenant;
  }
}
