package ch.verno.ui.base.settings.grid;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseSettingGrid<T> extends VerticalLayout {

  @Nullable protected Grid<T> grid;
  @Nonnull protected final Map<String, Grid.Column<T>> columnsByKey;

  protected BaseSettingGrid() {
    this.columnsByKey = new LinkedHashMap<>();
  }

  public void initUI() {
    setSizeFull();
    setPadding(false);
    setMargin(false);

    initGrid();
    add(grid);
  }

  protected void initGrid() {
    grid = new Grid<>();

    final var columns = getColumns();
    columns.forEach((valueProvider, header) -> addColumn(header, valueProvider));

    final var items = fetchItems();
    grid.setItems(items);

    setDefaultSorting();
  }

  private void addColumn(@Nonnull final String header,
                         @Nonnull final ValueProvider<T, Object> valueProvider) {
    if (grid == null) {
      throw new IllegalStateException("Grid has not been initialized. Call initGrid() first.");
    }

    grid.addColumn(valueProvider)
            .setHeader(header)
            .setSortable(true)
            .setResizable(true)
            .setAutoWidth(true);

    this.columnsByKey.put(header, grid.getColumnByKey(header));
  }

  public void addItemDoubleClickListener(@Nonnull final ComponentEventListener<ItemDoubleClickEvent<T>> listener) {
    if (grid != null) {
      grid.addItemDoubleClickListener(listener);
    }
  }

  public void refresh() {
    if (grid == null) {
      return;
    }

    final var items = fetchItems();
    grid.setItems(items);

    setDefaultSorting();
  }

  @Nonnull
  protected abstract List<T> fetchItems();

  @Nonnull
  protected abstract Map<ValueProvider<T, Object>, String> getColumns();

  @Nullable
  protected abstract String getDefaultSortColumnKey();

  protected void setDefaultSorting() {
    final var sortColumnKey = getDefaultSortColumnKey();
    if (sortColumnKey == null) {
      return;
    }

    final var column = columnsByKey.get(sortColumnKey);
    if (column != null && grid != null) {
      grid.sort(GridSortOrder.asc(column).build());
    }
  }
}