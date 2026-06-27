package ch.verno.server.bo.table.user;

import ch.verno.contract.dto.response.base.delete.DeleteErrorCode;
import ch.verno.contract.dto.response.base.save.SaveErrorCode;
import ch.verno.contract.dto.response.base.save.SaveResponse;
import ch.verno.contract.dto.table.user.AppUserDto;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.base.IBusinessObject;
import ch.verno.server.service.intern.table.user.AppUserService;
import jakarta.annotation.Nonnull;

public class AppUserBo implements IBusinessObject {

  @Nonnull private final Lazy<AppUserService> appuserService;

  protected AppUserBo(@Nonnull final ServerBean serverBean) {
    this.appuserService = Lazy.of(() -> serverBean.get(AppUserService.class));
  }

  @Nonnull
  public SaveResponse<AppUserDto> changePassword(@Nonnull final Long userId,
                                                 @Nonnull final String newPassword) {
    final var userOptional = appuserService.get().findById(userId);
    if (userOptional.isEmpty()) {
      return SaveResponse.failed(SaveErrorCode.NOT_FOUND);
    }

    final var user = userOptional.get();
    user.setPasswordHash(newPassword);

    final var saveResult = appuserService.get().save(user);
    return SaveResponse.success(saveResult);
  }

}
