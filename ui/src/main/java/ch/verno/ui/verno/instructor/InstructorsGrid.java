package ch.verno.ui.verno.instructor;

import ch.verno.common.db.dto.table.InstructorDto;
import ch.verno.common.db.filter.InstructorFilter;
import ch.verno.common.server.service.intern.IInstructorService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.ui.base.components.badge.VABadgeLabelOptions;
import ch.verno.publ.Routes;
import ch.verno.ui.base.components.contextmenu.ActionDef;
import ch.verno.ui.base.components.grid.GridActionRoles;
import ch.verno.ui.base.factory.BadgeLabelFactory;
import ch.verno.ui.lib.pages.grid.BaseOverviewGrid;
import ch.verno.ui.lib.pages.grid.ComponentGridColumn;
import ch.verno.ui.lib.pages.grid.ObjectGridColumn;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@PermitAll
@Route(Routes.INSTRUCTORS)
@Menu(order = 2, icon = "vaadin:institution", title = "shared.instructors.overview")
public class InstructorsGrid extends BaseOverviewGrid<InstructorDto, InstructorFilter> implements HasDynamicTitle {

  @Nonnull
  private final IInstructorService instructorService;

  public InstructorsGrid(@Nonnull final GlobalInterface globalInterface,
                         final boolean showGridToolbar,
                         final boolean showFilterToolbar) {
    super(globalInterface, InstructorFilter.empty(), showGridToolbar, showFilterToolbar);
    this.instructorService = globalInterface.getService(IInstructorService.class);
  }

  @Autowired
  public InstructorsGrid(@Nonnull final GlobalInterface globalInterface) {
    super(globalInterface, InstructorFilter.empty(), true, true);
    this.instructorService = globalInterface.getService(IInstructorService.class);
  }

  @Nonnull
  @Override
  protected Stream<InstructorDto> fetch(@Nonnull final Query<InstructorDto, InstructorFilter> query, @Nonnull final InstructorFilter filter) {
    final var offset = query.getOffset();
    final var limit = query.getLimit();
    final var sortOrders = query.getSortOrders();

    return instructorService.findInstructors(filter, offset, limit, sortOrders).stream();
  }

  @Nonnull
  @Override
  protected String getGridObjectName() {
    return getTranslation("shared.instructor");
  }

  @Nonnull
  @Override
  protected String getDetailPageRoute() {
    return Routes.createUrlFromUrlSegments(Routes.INSTRUCTORS, Routes.DETAIL);
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
  protected List<ActionDef> buildContextMenuActions(@Nonnull final InstructorDto dto) {
    final var actions = new ArrayList<ActionDef>();
    actions.add(ActionDef.create(
            "Delete Instructor",
            VaadinIcon.TRASH,
            () -> deleteInstructor(dto),
            isInstructorDeletable(dto)
    ));

    return actions;
  }

  private void deleteInstructor(@Nonnull final InstructorDto dto) {
    instructorService.deleteInstructor(dto.getId());
    refreshGrid();
  }

  private boolean isInstructorDeletable(@Nonnull final InstructorDto dto) {
    return !instructorService.isInstructorReferenced(dto.getId());
  }

  @Nonnull
  @Override
  protected List<ObjectGridColumn<InstructorDto>> getColumns() {
    final var columns = new ArrayList<ObjectGridColumn<InstructorDto>>();
    columns.add(new ObjectGridColumn<>("lastname", InstructorDto::getLastName, getTranslation("shared.last.name"), true));
    columns.add(new ObjectGridColumn<>("firstname", InstructorDto::getFirstName, getTranslation("shared.first.name"), true));
    columns.add(new ObjectGridColumn<>("gender", InstructorDto::genderAsString, getTranslation("shared.gender"), true));
    columns.add(new ObjectGridColumn<>("email", InstructorDto::getEmail, getTranslation("shared.e.mail"), true));
    columns.add(new ObjectGridColumn<>("phone", InstructorDto::phoneAsString, getTranslation("shared.phone"), true));
    columns.add(new ObjectGridColumn<>("address", (dto) -> dto.getAddress().getFullAddressAsString(), getTranslation("shared.address"), true));
    return columns;
  }

  @Nonnull
  @Override
  protected List<ComponentGridColumn<InstructorDto>> getComponentColumns() {
    final var components = new ArrayList<ComponentGridColumn<InstructorDto>>();
    components.add(new ComponentGridColumn<>("status", this::getInstructorStatusBadge, getTranslation("shared.status"), false, GridActionRoles.STICK_COLUMN));
    components.add(new ComponentGridColumn<>("actionColumn", this::getActionContextMenuButton, getTranslation("shared.action"), false, GridActionRoles.STICK_COLUMN));
    return components;
  }

  @Nonnull
  private Span getInstructorStatusBadge(@Nonnull final InstructorDto dto) {
    final var referenced = instructorService.isInstructorReferenced(dto.getId());
    if (referenced) {
      final var badgeLabel = BadgeLabelFactory.createBadgeLabel(getTranslation("common.in.verwendung"), VABadgeLabelOptions.SUCCESS);
      badgeLabel.setTooltipText(getTranslation("common.dieser.kursleiter.ist.in.verwendung.dadurch.kann.er.nicht.geloscht.werden"));
      return badgeLabel;
    } else {
      final var badgeLabel = BadgeLabelFactory.createBadgeLabel(getTranslation("common.nicht.in.verwendung"), VABadgeLabelOptions.NORMAL);
      badgeLabel.setTooltipText(getTranslation("common.dieser.kursleiter.ist.nicht.in.verwendung.dadurch.kann.er.geloscht.werden"));
      return badgeLabel;
    }
  }

  @Nonnull
  private Span getActionContextMenuButton(@Nonnull final InstructorDto dto) {
    final var button = new Button(VaadinIcon.ELLIPSIS_DOTS_V.create());
    button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

    final var menu = new ContextMenu(button);
    menu.setOpenOnClick(true);

    menu.removeAll();
    for (final var action : buildContextMenuActions(dto)) {
      final var item = menu.addItem(action.getComponent(), ev -> action.getRunnable().run());
      item.setEnabled(action.isEnabled());
    }

    return new Span(button);
  }

  @Nonnull
  @Override
  protected InstructorFilter withSearchText(@Nonnull final String searchText) {
    return InstructorFilter.ofSearchText(searchText);
  }

  @Override
  protected void setDefaultSorting() {
    final var lastNameCol = columnsByKey.get("lastname");
    if (lastNameCol == null) {
      return;
    }

    grid.sort(List.of(new GridSortOrder<>(lastNameCol, SortDirection.ASCENDING)));
  }

  @Override
  public String getPageTitle() {
    return getTranslation("shared.instructors.overview");
  }
}
