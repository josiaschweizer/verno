package ch.verno.ui.verno.settings.setting.user;

import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.common.db.service.IAppUserService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.ui.base.settings.grid.BaseSettingGrid;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@UIScope
@SpringComponent
public class AppUserGrid extends BaseSettingGrid<AppUserDto> {

  @Nonnull private final IAppUserService appUserService;

  public AppUserGrid(@Nonnull final GlobalInterface globalInterface) {
    this.appUserService = globalInterface.getService(IAppUserService.class);
  }

  @Nonnull
  @Override
  protected List<AppUserDto> fetchItems() {
    return appUserService.getAllAppUsers();
  }

  @Nonnull
  @Override
  protected Map<ValueProvider<AppUserDto, Object>, String> getColumns() {
    final var columnsMap = new LinkedHashMap<ValueProvider<AppUserDto, Object>, String>();
    columnsMap.put(AppUserDto::getUsername, getTranslation("shared.username"));
    columnsMap.put(AppUserDto::getRole, getTranslation("shared.role"));
    columnsMap.put(appUser -> appUser.isActive() ? getTranslation("common.yes") : getTranslation("common.no"), getTranslation("shared.active"));
    return columnsMap;
  }

  @Nullable
  @Override
  protected String getDefaultSortColumnKey() {
    return getTranslation("shared.username");
  }
}