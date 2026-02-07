package ch.verno.ui.base.pages.grid;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.publ.Routes;
import ch.verno.ui.base.components.contextmenu.ActionDef;
import ch.verno.ui.base.components.filter.FilterEntryFactory;
import ch.verno.ui.base.components.filter.VAFilterBar;
import ch.verno.ui.base.components.grid.GridActionRoles;
import ch.verno.ui.base.components.grid.VAGrid;
import ch.verno.ui.base.components.toolbar.ViewToolbar;
import ch.verno.ui.base.components.toolbar.ViewToolbarFactory;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBoxBase;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import jakarta.annotation.Nonnull;

import java.util.*;
import java.util.stream.Stream;

public abstract class BaseOverviewGrid<T extends BaseDto, F> extends VerticalLayout {

  @Nonnull protected final VAGrid<T> grid;
  @Nonnull protected final Map<String, Grid.Column<T>> columnsByKey;
  @Nonnull private final ConfigurableFilterDataProvider<T, Void, F> dataProvider;
  @Nonnull private F filter;
  @Nonnull protected final FilterEntryFactory<F> filterEntryFactory;
  @Nonnull protected final Binder<F> filterBinder;

  protected boolean showGridToolbar = true;
  protected boolean showFilterToolbar = true;

  private List<T> cachedData = new ArrayList<>();
  private F cachedFilter = null;
  private List<?> cachedSortOrders = null;
  private final Object cacheLock = new Object();

  protected BaseOverviewGrid(@Nonnull final F initialFilter,
                             final boolean showGridToolbar,
                             final boolean showFilterToolbar) {
    this(initialFilter);
    this.showGridToolbar = showGridToolbar;
    this.showFilterToolbar = showFilterToolbar;
  }

  protected BaseOverviewGrid(@Nonnull final F initialFilter) {
    this.columnsByKey = new HashMap<>();
    this.filter = initialFilter;
    this.grid = new VAGrid<>();
    this.filterEntryFactory = new FilterEntryFactory<>();
    this.filterBinder = new Binder<>();

    final var backendProvider = DataProvider.fromFilteringCallbacks(
            this::fetchFromBackend,
            this::countFromBackend
    );
    this.dataProvider = backendProvider.withConfigurableFilter();

    setSizeFull();
    setPadding(false);
    setSpacing(false);

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

    final var componentsToAdd = new ArrayList<Component>();
    if (showGridToolbar) {
      componentsToAdd.add(gridToolbar);
    }
    if (showFilterToolbar) {
      componentsToAdd.add(filterBar);
    }
    componentsToAdd.add(grid);
    add(componentsToAdd);
  }

  @Nonnull
  protected ViewToolbar createGridToolbar() {
    return ViewToolbarFactory.createGridToolbar(getGridObjectName(), getDetailPageRoute());
  }

  @Nonnull
  private VerticalLayout createFilterBar() {
    final var filterBar = new VAFilterBar();
    getFilterComponents().forEach(filterBar::addFilterComponent);
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
    filterBarLayout.getStyle().setPaddingLeft("5px");
    filterBarLayout.getStyle().setPaddingRight("5px");
    return filterBarLayout;
  }

  protected void initGrid() {
    final var columns = getColumns();
    columns.forEach(this::addColumn);
    final var componentColumns = getComponentColumns();
    componentColumns.forEach(this::addComponentColumn);

    grid.addItemDoubleClickListener(this::onGridItemDoubleClick);
    grid.addSortListener(sort -> {
      System.out.println("Sort event: " + sort.getSortOrder());
    });
    setDefaultSorting();
    dataProvider.setFilter(filter);
  }

  public void setFilter(@Nonnull final F newFilter) {
    this.filter = newFilter;
    // Invalidate cache when filter changes
    synchronized (cacheLock) {
      cachedFilter = null;
      cachedData = new ArrayList<>();
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
  public List<ComboBoxBase<?, ?, ?>> getFilterComponents() {
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
    cachedSortOrders = new ArrayList<>(query.getSortOrders());
  }

  protected void onGridItemDoubleClick(@Nonnull final ItemDoubleClickEvent<T> event) {
    navigateToDetail(event.getItem());
  }

  protected void navigateToDetail(@Nonnull final T dto) {
    final var url = Routes.getDetailURL(this.getClass());
    final var redirectURL = Routes.getURLWithId(url, dto.getId());
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
    // Default implementation returns an empty context menu -> to be implemented by subclasses if needed
  }

  protected List<ActionDef> buildContextMenuActions(@Nonnull final T dto) {
    // Default implementation returns an empty list - to be implemented by subclass if needed
    // normally used by createContextMenu to get all context menu items and use the items at the same time
    // in the sticky action slot at the end of the grid
    return new ArrayList<>();
  }

  @Nonnull
  protected abstract Stream<T> fetch(@Nonnull Query<T, F> query, @Nonnull F filter);

  @Nonnull
  protected abstract String getGridObjectName();

  @Nonnull
  protected abstract String getDetailPageRoute();

  @Nonnull
  protected abstract List<ObjectGridColumn<T>> getColumns();

  @Nonnull
  protected List<ComponentGridColumn<T>> getComponentColumns() {
    // Default implementation returns no component columns -> to be implemented by subclasses if needed
    return new ArrayList<>();
  }
}