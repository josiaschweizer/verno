package ch.verno.ui.verno.usermanagemnt;

import ch.verno.common.ui.base.components.badge.VABadgeLabelOptions;
import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.common.db.filter.AppUserFilter;
import ch.verno.common.db.role.Role;
import ch.verno.common.db.service.IAppUserService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.ui.dto.UserDtoUnhashedPw;
import ch.verno.publ.Routes;
import ch.verno.ui.base.components.contextmenu.ActionDef;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.factory.BadgeLabelFactory;
import ch.verno.ui.base.factory.SpanFactory;
import ch.verno.ui.base.pages.grid.BaseOverviewGrid;
import ch.verno.ui.base.pages.grid.ComponentGridColumn;
import ch.verno.ui.base.pages.grid.GridColumnHelper;
import ch.verno.ui.base.pages.grid.ObjectGridColumn;
import ch.verno.ui.lib.icon.CustomIconConstants;
import ch.verno.ui.verno.usermanagemnt.dialog.ChangePasswordDialog;
import ch.verno.ui.verno.usermanagemnt.dialog.CreateUserDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.annotation.security.RolesAllowed;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Route(Routes.APP_USERS)
@RolesAllowed({"ADMIN"})
@Menu(order = 99, icon = CustomIconConstants.USER_COG, title = "shared.application.users")
public class UsersGrid extends BaseOverviewGrid<AppUserDto, AppUserFilter> implements HasDynamicTitle {

  @Nonnull private final IAppUserService appUserService;
  private final GlobalInterface globalInterface;

  protected UsersGrid(@Nonnull final GlobalInterface globalInterface) {
    super(globalInterface, AppUserFilter.empty(), true, false);

    this.globalInterface = globalInterface;
    this.appUserService = globalInterface.getService(IAppUserService.class);
  }

  @Nonnull
  @Override
  protected Stream<AppUserDto> fetch(@Nonnull final Query<AppUserDto, AppUserFilter> query,
                                     @Nonnull final AppUserFilter filter) {
    final var offset = query.getOffset();
    final var limit = query.getLimit();
    final var sortOrders = query.getSortOrders();

    return appUserService.findUsers(filter, offset, limit, sortOrders).stream();
  }

  @Nonnull
  @Override
  protected String getGridObjectName() {
    return getTranslation("shared.application.user");
  }

  @Override
  protected boolean hasDetailPage() {
    return false;
  }

  @Nullable
  @Override
  protected Runnable getCustomActionButtonRunnable() {
    return () -> openUserDialog(null);
  }

  @Nonnull
  @Override
  protected List<ObjectGridColumn<AppUserDto>> getColumns() {
    final var columns = new ArrayList<ObjectGridColumn<AppUserDto>>();
    columns.add(GridColumnHelper.objectCol("username", AppUserDto::getUsername, getTranslation("shared.username"), true));
    return columns;
  }

  @Nonnull
  @Override
  protected List<ComponentGridColumn<AppUserDto>> getComponentColumns() {
    final var columns = new ArrayList<ComponentGridColumn<AppUserDto>>();
    columns.add(GridColumnHelper.componentCol("role", user -> createRoleBadge(user.getRole()), getTranslation("shared.role"), true));
    columns.add(GridColumnHelper.componentCol("active", user -> createActiveBadge(user.isActive()), getTranslation("shared.active"), true));
    return columns;
  }

  @Nonnull
  private Component createRoleBadge(@Nonnull final Role role) {
    return BadgeLabelFactory.createBadgeLabel(getTranslation(role.getRoleNameKey()), role.getBadgeLabelOptions());
  }

  @Nonnull
  private Component createActiveBadge(final boolean active) {
    if (active) {
      return BadgeLabelFactory.createBadgeLabel(getTranslation("shared.active"), VABadgeLabelOptions.SUCCESS);
    } else {
      return BadgeLabelFactory.createBadgeLabel(getTranslation("shared.inactive"), VABadgeLabelOptions.ERROR);
    }
  }

  @Override
  public void createContextMenu() {
    final var gridContextMenu = grid.addContextMenu();

    gridContextMenu.setDynamicContentHandler(dto -> {
      gridContextMenu.removeAll();
      if (dto == null) {
        return false;
      }

      for (final var action : buildContextMenuActions(dto)) {
        final var item = gridContextMenu.addItem(action.getComponent(), e -> action.getRunnable().run());
        item.setEnabled(action.isEnabled());
      }
      return true;
    });
  }

  @Override
  protected List<ActionDef> buildContextMenuActions(@Nonnull final AppUserDto dto) {
    final var currentUser = globalInterface.getCurrentUser();
    final var actions = new ArrayList<ActionDef>();

    actions.add(ActionDef.create(
            SpanFactory.createSpan(getTranslation("shared.edit.user"), VaadinIcon.EDIT),
            () -> openUserDialog(dto),
            dto.isActive()
    ));
    actions.add(ActionDef.create(
            SpanFactory.createSpan(getTranslation("shared.change.password", dto.getUsername()), VaadinIcon.KEY),
            () -> openChangePasswordDialog(dto),
            dto.isActive()
    ));

    if (dto.isActive()) {
      actions.add(ActionDef.create(
              SpanFactory.createSpan(getTranslation("shared.disable.user"), VaadinIcon.BAN),
              () -> disableItem(dto),
              !Objects.equals(dto.getId(), currentUser.getId())
      ));
    } else {
      actions.add(ActionDef.create(
              SpanFactory.createSpan(getTranslation("shared.enable.user"), VaadinIcon.CHECK_CIRCLE),
              () -> enableItem(dto)
      ));
    }

    actions.add(ActionDef.create(
            SpanFactory.createSpan(getTranslation("shared.delete"), VaadinIcon.TRASH),
            () -> deleteItem(dto),
            !Objects.equals(dto.getId(), currentUser.getId())
    ));

    return actions;
  }

  private void openUserDialog(@Nullable final AppUserDto dto) {
    CreateUserDialog dialog;
    if (dto == null) {
      dialog = new CreateUserDialog(globalInterface);
    } else {
      dialog = new CreateUserDialog(globalInterface, FormMode.EDIT, UserDtoUnhashedPw.fromAppUserDto(dto));
    }

    dialog.addClosedListener(e -> setFilter(getFilter())); // refresh grid by reapplying filter when dialog is closed
    dialog.open();
  }

  private void openChangePasswordDialog(@Nonnull final AppUserDto dto) {
    if (dto.getId() == null) {
      return;
    }

    final var dialog = new ChangePasswordDialog(globalInterface, dto.getId());
    dialog.open();
  }

  private void disableItem(@Nonnull final AppUserDto dto) {
    dto.setActive(false);
    appUserService.updateAppUser(dto);
    refreshGrid();
  }

  private void enableItem(@Nonnull final AppUserDto dto) {
    dto.setActive(true);
    appUserService.updateAppUser(dto);
    refreshGrid();
  }

  private void deleteItem(@Nonnull final AppUserDto dto) {
    final var confirm = new ConfirmDialog(getTranslation("shared.delete"), getTranslation("shared.are.you.sure.you.want.to.delete.this.user"), getTranslation("shared.confirm"), e -> confirmDelete(dto), getTranslation("shared.cancel"), e -> confirmDelete(null));
    confirm.open();
  }

  private void confirmDelete(@Nullable final AppUserDto dto) {
    if (dto != null && dto.getId() != null) {
      appUserService.deleteAppUser(dto.getId());
      setFilter(getFilter()); // refresh grid by reapplying filter
    }
  }

  @Override
  public String getPageTitle() {
    return getTranslation("shared.application.user");
  }

  @Override
  protected void onGridItemDoubleClick(@Nonnull final ItemDoubleClickEvent<AppUserDto> event) {
    final var dto = event.getItem();
    if (dto.getId() == null) {
      return;
    }

    openUserDialog(dto);
  }
}
