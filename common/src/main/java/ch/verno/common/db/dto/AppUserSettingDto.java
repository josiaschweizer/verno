package ch.verno.common.db.dto;

import ch.verno.common.db.dto.base.BaseDto;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class AppUserSettingDto extends BaseDto {

  @Nonnull
  private Long userId;

  @Nullable
  private String theme;

  public AppUserSettingDto() {
    userId = 0L;
  }

  public AppUserSettingDto(@Nonnull final Long userId,
                           @Nonnull final String theme) {
    this.userId = userId;
    this.theme = theme;
  }

  public AppUserSettingDto(@Nullable final Long id,
                           @Nonnull final Long userId,
                           @Nullable final String theme) {
    setId(id);
    this.userId = userId;
    this.theme = theme;
  }

  @Nonnull
  public Long getUserId() {
    return userId;
  }

  public void setUserId(@Nonnull final Long userId) {
    this.userId = userId;
  }

  @Nullable
  public String getTheme() {
    return theme;
  }

  public void setTheme(@Nullable final String theme) {
    this.theme = theme;
  }
}