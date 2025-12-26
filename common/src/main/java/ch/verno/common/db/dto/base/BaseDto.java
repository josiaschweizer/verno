package ch.verno.common.db.dto.base;

import jakarta.annotation.Nullable;

public class BaseDto {

  @Nullable
  private Long id;

  @Nullable
  public Long getId() {
    return id;
  }

  public void setId(@Nullable final Long id) {
    this.id = id;
  }
}
