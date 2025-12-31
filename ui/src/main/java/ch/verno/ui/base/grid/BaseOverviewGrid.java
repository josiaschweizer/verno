package ch.verno.ui.base.grid;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.ui.base.components.filter.FilterEntryFactory;
import ch.verno.ui.base.components.filter.VAFilterBar;
import ch.verno.ui.base.components.toolbar.ViewToolbarFactory;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.function.ValueProvider;
import jakarta.annotation.Nonnull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public abstract class BaseOverviewGrid<T extends BaseDto, F> extends VerticalLayout {

  @Nonnull
  protected final Grid<T> grid;
  @Nonnull
  protected final Map<String, Grid.Column<T>> columnsByKey;
  @Nonnull
  private final ConfigurableFilterDataProvider<T, Void, F> dataProvider;
  @Nonnull
  private F filter;
  @Nonnull
  protected final FilterEntryFactory<F> filterEntryFactory;
  @Nonnull
  protected final Binder<F> filterBinder;

  protected boolean showGridToolbar = true;

  protected BaseOverviewGrid(@Nonnull final F initialFilter, final boolean showGridToolbar) {
    this(initialFilter);
    this.showGridToolbar = showGridToolbar;
  }

  protected BaseOverviewGrid(@Nonnull final F initialFilter) {
    this.columnsByKey = new HashMap<>();
    this.filter = initialFilter;
    this.grid = new Grid<>();
    this.filterEntryFactory = new FilterEntryFactory<>();
    this.filterBinder = new Binder<>();

    final var callbackProvider = DataProvider.fromFilteringCallbacks(this::fetchFromBackend, this::countFromBackend);
    this.dataProvider = callbackProvider.withConfigurableFilter();

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
    final var gridToolbar = ViewToolbarFactory.createGridToolbar(getGridObjectName(), getDetailPageRoute());
    final var filterBar = createFilterBar();

    if (showGridToolbar) {
      add(gridToolbar, filterBar, grid);
    } else {
      add(filterBar, grid);
    }
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
    columns.forEach((valueProvider, header) -> addColumn(header, valueProvider));

    grid.addItemDoubleClickListener(this::onGridItemDoubleClick);
    setDefaultSorting();
    dataProvider.setFilter(filter);
  }

  public void setFilter(@Nonnull final F newFilter) {
    this.filter = newFilter;
    // Update binder with new filter values
    filterBinder.readBean(this.filter);
    dataProvider.setFilter(this.filter);
    dataProvider.refreshAll();
  }

  @Nonnull
  public F getFilter() {
    return filter;
  }

  @Nonnull
  public List<MultiSelectComboBox<Long>> getFilterComponents() {
    return List.of();
  }

  @Nonnull
  private Stream<T> fetchFromBackend(@Nonnull final Query<T, F> query) {
    final var effectiveFilter = query.getFilter().orElse(filter);
    return fetch(query, effectiveFilter);
  }

  private int countFromBackend(@Nonnull final Query<T, F> query) {
    final var effectiveFilter = query.getFilter().orElse(filter);
    return count(query, effectiveFilter);
  }

  private void onGridItemDoubleClick(@Nonnull final ItemDoubleClickEvent<T> event) {
    final var url = Routes.getDetailURL(this.getClass());
    final var redirectURL = Routes.getURLWithId(url, event.getItem().getId());
    UI.getCurrent().navigate(redirectURL);
  }

  private void addColumn(@Nonnull final String header,
                         @Nonnull final ValueProvider<T, Object> valueProvider) {
    final var col = grid.addColumn(valueProvider)
            .setHeader(header)
            .setKey(header)
            .setSortable(true)
            .setResizable(true)
            .setAutoWidth(true);

    this.columnsByKey.put(header, col);
  }

  protected void setDefaultSorting() {
    // override optional
  }

  @Nonnull
  protected F withSearchText(@Nonnull final String searchText) {
    return getFilter(); // default: no search text applied
  }

  @Nonnull
  protected abstract Stream<T> fetch(@Nonnull Query<T, F> query, @Nonnull F filter);

  protected abstract int count(@Nonnull Query<T, F> query, @Nonnull F filter);

  @Nonnull
  protected abstract String getGridObjectName();

  protected abstract String getDetailPageRoute();

  @Nonnull
  protected abstract Map<ValueProvider<T, Object>, String> getColumns();
}