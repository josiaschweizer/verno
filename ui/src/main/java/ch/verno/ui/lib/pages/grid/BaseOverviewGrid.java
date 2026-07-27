package ch.verno.ui.lib.pages.grid;

import ch.verno.contract.dto.table.base.BaseDto;
import ch.verno.lib.New;
import ch.verno.lib.Publ;
import ch.verno.lib.VernoUtility;
import ch.verno.ui.base.components.contextmenu.ActionDef;
import ch.verno.ui.base.components.emptystate.VAEmptyState;
import ch.verno.ui.base.components.filter.FilterEntryFactory;
import ch.verno.ui.base.components.filter.VAFilterBar;
import ch.verno.ui.base.components.grid.GridActionRoles;
import ch.verno.ui.base.components.grid.VAGrid;
import ch.verno.ui.base.components.toolbar.ViewToolbar;
import ch.verno.ui.base.components.toolbar.ViewToolbarFactory;
import ch.verno.ui.lib.url.RoutesUtil;
import com.google.inject.Injector;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBoxBase;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NonNls;

import java.util.*;
import java.util.stream.Stream;

public abstract class BaseOverviewGrid<T extends BaseDto<?>, F> extends VerticalLayout {

  @NonNls public static final String GRID_COLUMN_ACTION_COLUMN = "action-column";

  @Nonnull protected final Injector injector;

  @Nonnull protected final VAGrid<T> grid;
  @Nonnull protected final Map<String, Grid.Column<T>> columnsByKey;
  @Nonnull private final ConfigurableFilterDataProvider<T, Void, F> dataProvider;

  @Nonnull private F filter;
  @Nonnull protected final Binder<F> filterBinder;
  @Nonnull protected final FilterEntryFactory<F> filterEntryFactory;

  protected boolean showGridToolbar = true;
  protected boolean showFilterToolbar = true;

  @Nonnull private List<T> cachedData;
  @Nullable private F cachedFilter;
  @Nullable private List<?> cachedSortOrders;
  @Nonnull private final Object cacheLock;

  protected BaseOverviewGrid(@Nonnull final Injector injector,
                             @Nonnull final F initialFilter,
                             final boolean showGridToolbar,
                             final boolean showFilterToolbar) {
    this(injector, initialFilter);
    this.showGridToolbar = showGridToolbar;
    this.showFilterToolbar = showFilterToolbar;
  }

  protected BaseOverviewGrid(@Nonnull final Injector injector,
                             @Nonnull final F initialFilter) {
    this.injector = injector;
    this.columnsByKey = New.map();

    this.filter = initialFilter;
    this.filterBinder = new Binder<>();
    this.filterEntryFactory = new FilterEntryFactory<>();

    this.cachedData = New.list();
    this.cachedFilter = null;
    this.cachedSortOrders = null;
    this.cacheLock = new Object();

    final var backendProvider = DataProvider.fromFilteringCallbacks(
            this::fetchFromBackend,
            this::countFromBackend
    );
    this.dataProvider = backendProvider.withConfigurableFilter();

    setSizeFull();
    setPadding(false);
    setSpacing(false);

    this.grid = new VAGrid<>();
    grid.setSizeFull();
    grid.setDataProvider(dataProvider);
  }

  @Override
  protected void onAttach(@Nonnull final AttachEvent attachEvent) {
    super.onAttach(attachEvent);
    initUI(showGridToolbar);
  }

  protected void initUI(final boolean showGridToolbar) {
    initGrid();
    createContextMenu();

    final var gridToolbar = createGridToolbar();
    final var filterBar = createFilterBar();

    final var componentsToAdd = New.<Component>arrayList();
    if (showGridToolbar) {
      componentsToAdd.add(gridToolbar);
    }
    if (showFilterToolbar) {
      componentsToAdd.add(filterBar);
    }
    componentsToAdd.add(grid);

    final var customComponents = getCustomComponents();
    customComponents.forEach((index, component) -> {
      if (componentsToAdd.size() < index) {
        componentsToAdd.addLast(component);
      } else if (index <= 0) {
        componentsToAdd.addFirst(component);
      } else {
        componentsToAdd.add(index, component);
      }
    });

    add(componentsToAdd);
  }

  @Nonnull
  protected ViewToolbar createGridToolbar() {
    if (hasDetailPage()) {
      return ViewToolbarFactory.createGridToolbar(injector, getGridObjectName(), getDetailPageRoute());
    }

    final var customRunnable = getCustomActionButtonRunnable();
    if (customRunnable == null) {
      return ViewToolbarFactory.createGridToolbar(injector, getGridObjectName());
    }

    return ViewToolbarFactory.createGridToolbar(injector, getGridObjectName(), getCustomActionButtonRunnable());
  }

  @Nonnull
  private VerticalLayout createFilterBar() {
    final var filterBar = injector.getInstance(VAFilterBar.class);
    getFilterBarComponents().forEach(filterBar::addFilterComponent);
    filterBar.setSearchHandler(searchText -> setFilter(withSearchText(searchText)));
    filterBar.setOnFiltersChanged(() -> {
      try {
        filterBinder.writeBean(filter);
      } catch (ValidationException e) {
        // Ignore validation errors for filters
      }
      synchronized (cacheLock) {
        cachedFilter = null;
        cachedData = new ArrayList<>();
      }
      dataProvider.setFilter(filter);
      dataProvider.refreshAll();
    });

    filterBinder.readBean(filter);

    final var filterBarLayout = new VerticalLayout(filterBar);
    filterBarLayout.setPadding(false);
    filterBarLayout.getStyle().setPaddingLeft(VernoUtility.LUMO_SPACE_XS);
    filterBarLayout.getStyle().setPaddingRight(VernoUtility.LUMO_SPACE_XS);
    return filterBarLayout;
  }

  protected void initGrid() {
    final var prefixComponentColumns = getPrefixComponentColumns();
    prefixComponentColumns.forEach(this::addComponentColumn);
    final var columns = getColumns();
    columns.forEach(this::addColumn);
    final var componentColumns = getComponentColumns();
    componentColumns.forEach(this::addComponentColumn);

    grid.addItemDoubleClickListener(this::onGridItemDoubleClick);
    grid.setEmptyStateComponent(createEmptyState());

    setDefaultSorting();
    dataProvider.setFilter(filter);
  }

  public void setFilter(@Nonnull final F newFilter) {
    this.filter = newFilter;
    // Invalidate cache when filter changes
    synchronized (cacheLock) {
      cachedFilter = null;
      cachedData = New.arrayList();
    }

    filterBinder.readBean(this.filter);
    dataProvider.setFilter(this.filter);
    dataProvider.refreshAll();
  }

  @Nonnull
  public F getFilter() {
    return filter;
  }

  @Nonnull
  public List<ComboBoxBase<?, ?, ?>> getFilterBarComponents() {
    return List.of();
  }

  @Nonnull
  private Stream<T> fetchFromBackend(@Nonnull final Query<T, F> query) {
    synchronized (cacheLock) {
      final var effectiveFilter = query.getFilter().orElse(filter);
      final var sortOrders = query.getSortOrders();
      final int offset = query.getOffset();
      final int limit = query.getLimit();

      if (!Objects.equals(cachedFilter, effectiveFilter) || !Objects.equals(cachedSortOrders, sortOrders)) {
        refreshCache(query, effectiveFilter);
      }

      if (offset >= cachedData.size()) {
        return Stream.empty();
      }

      final int endIndex = Math.min(offset + limit, cachedData.size());
      return cachedData.subList(offset, endIndex).stream();
    }
  }

  private int countFromBackend(@Nonnull final Query<T, F> query) {
    synchronized (cacheLock) {
      final var effectiveFilter = query.getFilter().orElse(filter);
      final var sortOrders = query.getSortOrders();

      if (!Objects.equals(cachedFilter, effectiveFilter) || !Objects.equals(cachedSortOrders, sortOrders)) {
        refreshCache(query, effectiveFilter);
      }

      return cachedData.size();
    }
  }

  private void refreshCache(@Nonnull final Query<T, F> query, @Nonnull final F effectiveFilter) {
    final var allDataQuery = new Query<T, F>(0, Integer.MAX_VALUE, query.getSortOrders(), null, effectiveFilter);
    cachedData = fetch(allDataQuery, effectiveFilter).toList();
    cachedFilter = effectiveFilter;
    cachedSortOrders = New.arrayList(query.getSortOrders());
  }

  protected void onGridItemDoubleClick(@Nonnull final ItemDoubleClickEvent<T> event) {
    navigateToDetail(event.getItem());
  }

  protected void navigateToDetail(@Nonnull final T dto) {
    final var url = RoutesUtil.getDetailURL(this.getClass());
    final var redirectURL = RoutesUtil.getURLWithId(url, dto.getId());
    UI.getCurrent().navigate(redirectURL);
  }

  private void addColumn(@Nonnull final ObjectGridColumn<T> gridColumn) {
    final var col = grid.addColumn(gridColumn.valueProvider())
            .setHeader(gridColumn.header())
            .setKey(gridColumn.key())
            .setSortable(gridColumn.sortable())
            .setResizable(true)
            .setAutoWidth(true);

    this.columnsByKey.put(gridColumn.key(), col);
  }

  private void addComponentColumn(@Nonnull final ComponentGridColumn<T> gridColumn) {
    final var col = grid.addComponentColumn(gridColumn.component())
            .setHeader(gridColumn.header())
            .setKey(gridColumn.key())
            .setSortable(gridColumn.sortable())
            .setResizable(true)
            .setAutoWidth(true);

    final var actions = gridColumn.actionRoles();
    if (actions != null) {
      for (final var action : actions) {
        if (action.equals(GridActionRoles.STICK_COLUMN)) {
          col.setFrozenToEnd(true);
        } else if (action.equals(GridActionRoles.INVISIBLE_COLUMN)) {
          col.setVisible(false);
        }
      }
    }

    this.columnsByKey.put(gridColumn.key(), col);
  }

  protected void setDefaultSorting() {
    // override optional
  }

  @Nonnull
  protected F withSearchText(@Nonnull final String searchText) {
    return getFilter(); // default: no search text applied
  }

  @Nonnull
  public Grid<T> getGrid() {
    return grid;
  }

  public void createContextMenu() {
    grid.addContextMenu(this::buildContextMenuActions);
  }

  @Nonnull
  protected List<ActionDef> buildContextMenuActions(@Nonnull final T dto) {
    // Default implementation returns an empty list - to be implemented by subclass if needed
    // normally used by createContextMenu to get all context menu items and use the items at the same time
    // in the sticky action slot at the end of the grid
    return New.list();
  }

  @Nonnull
  protected abstract Stream<T> fetch(@Nonnull Query<T, F> query, @Nonnull F filter);

  @Nonnull
  protected abstract String getGridObjectName();

  @Nonnull
  protected abstract List<ObjectGridColumn<T>> getColumns();

  @Nonnull
  protected List<ComponentGridColumn<T>> getPrefixComponentColumns() {
    // Default implementation returns an empty list of component columns -> to be implemented by subclasses if needed
    return new ArrayList<>();
  }

  @Nonnull
  protected List<ComponentGridColumn<T>> getComponentColumns() {
    // Default implementation returns no component columns -> to be implemented by subclasses if needed
    return new ArrayList<>();
  }

  @Nonnull
  protected String getDetailPageRoute() {
    // Default implementation returns an empty route - override to provide a detail page route for this grid
    return Publ.EMPTY_STRING;
  }

  protected boolean hasDetailPage() {
    // Default implementation assumes there is a detail page - override if there is no detail page for this grid
    // -> THEN ALSO THE getCustomActionButtonRunnable TO BE OVERRIDDEN
    return true;
  }

  @Nullable
  protected Runnable getCustomActionButtonRunnable() {
    return null;
  }

  /**
   * Returns custom components that should be added to the UI at specific indexes.
   *
   * <p>Subclasses may override this method to insert additional components into
   * the UI in the desired order.</p>
   *
   * @return a map of UI indexes to custom components
   */
  @Nonnull
  protected HashMap<Integer, Component> getCustomComponents() {
    // can be overridden in subclass if needed
    return New.hashMap();
  }

  protected void refreshGrid() {
    grid.getDataProvider().refreshAll();
  }

  @Nonnull
  protected VAEmptyState createEmptyState() {
    final var emptyState = new VAEmptyState();
    emptyState.setHeightFull();
    emptyState.setBorderless(true);

    emptyState.setIcon(VaadinIcon.ROCKET);
    emptyState.setTitle(getTranslation("base.no.0.found", getGridObjectName()));
    emptyState.setDescription(getTranslation("base.erstelle.oben.rechts.einen.neuen.record.oder.passe.die.filter.an"));

    return emptyState;
  }
}