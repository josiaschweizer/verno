package ch.verno.server.bo.table.user;

import ch.verno.contract.dto.response.base.save.SaveErrorCode;
import ch.verno.contract.dto.response.base.save.SaveResponse;
import ch.verno.contract.dto.table.user.AppUserDto;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.base.IBusinessObject;
import ch.verno.server.service.entity.user.AppUserService;
import jakarta.annotation.Nonnull;

public class AppUserBo implements IBusinessObject {

  @Nonnull private final Lazy<AppUserService> appUserService;

  protected AppUserBo(@Nonnull final ServerBean serverBean) {
    this.appUserService = Lazy.of(() -> serverBean.get(AppUserService.class));
  }

  @Nonnull
  public SaveResponse<AppUserDto> changePassword(@Nonnull final Long userId,
                                                 @Nonnull final String newPassword) {
    final var userOptional = appUserService.get().findById(userId);
    if (userOptional.isEmpty()) {
      return SaveResponse.failed(SaveErrorCode.NOT_FOUND);
    }

    final var user = userOptional.get();
    user.setPasswordHash(newPassword);

    final var saveResult = appUserService.get().save(user);
    return SaveResponse.success(saveResult);
  }

}
