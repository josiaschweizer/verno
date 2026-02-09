package ch.verno.ui.verno.usermanagemnt.dialog;

import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;

public class ChangePasswordDto {

  @Nonnull private Long userId;
  @Nonnull private String newPassword;
  @Nonnull private String confirmNewPassword;

  public ChangePasswordDto() {
    userId = 0L;
    newPassword = Publ.EMPTY_STRING;
    confirmNewPassword = Publ.EMPTY_STRING;
  }

  public ChangePasswordDto(@Nonnull final Long userId) {
    this.userId = userId;
    this.newPassword = Publ.EMPTY_STRING;
    this.confirmNewPassword = Publ.EMPTY_STRING;
  }

  @Nonnull
  public Long getUserId() {
    return userId;
  }

  public void setUserId(@Nonnull final Long userId) {
    this.userId = userId;
  }

  @Nonnull
  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(@Nonnull final String newPassword) {
    this.newPassword = newPassword;
  }

  @Nonnull
  public String getConfirmNewPassword() {
    return confirmNewPassword;
  }

  public void setConfirmNewPassword(@Nonnull final String confirmNewPassword) {
    this.confirmNewPassword = confirmNewPassword;
  }
}
