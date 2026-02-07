package ch.verno.ui.verno.settings.setting.user;

import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.ui.base.settings.grid.BaseGridDetailSetting;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class AppUserSetting extends BaseGridDetailSetting<AppUserDto, AppUserGrid, AppUserDetail> {

  public static final String TITLE_KEY = "shared.application.user";

  public AppUserSetting(@Nonnull final GlobalInterface globalInterface) {
    super(
            TITLE_KEY,
            new AppUserGrid(globalInterface),
            new AppUserDetail(globalInterface)
    );
  }

  @Nonnull
  @Override
  protected String getAddButtonText() {
    return getTranslation("shared.add.new.user");
  }

  @Nonnull
  @Override
  protected String getBackButtonText() {
    return getTranslation("shared.back.to.users");
  }

  @Nullable
  @Override
  protected Long getEntityId(@Nonnull final AppUserDto entity) {
    return entity.getId();
  }

  @Nonnull
  @Override
  protected Class<AppUserDto> getBeanType() {
    return AppUserDto.class;
  }

  @Nonnull
  @Override
  protected AppUserDto createNewBeanInstance() {
    final var appUser = new AppUserDto();
    appUser.setActive(true);
    return appUser;
  }
}