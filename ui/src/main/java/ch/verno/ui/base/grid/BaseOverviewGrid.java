package ch.verno.ui.base.grid;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.ui.base.components.toolbar.ViewToolbarFactory;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseOverviewGrid<T extends BaseDto> extends VerticalLayout {

  @Nullable
  protected Grid<T> grid;

  @Nonnull
  protected final Map<String, Grid.Column<T>> columnsByKey;

  public BaseOverviewGrid() {
    this.columnsByKey = new HashMap<>();
    // empty constructor
  }

  @Override
  protected void onAttach(@Nonnull final AttachEvent attachEvent) {
    // Initialize UI only once. onAttach can be called multiple times when the
    // component is re-attached; avoid re-adding the toolbar/grid repeatedly
    // problem seen in the settings
    if (grid == null) {
      initUI();
    }
  }

  protected void initUI() {
    setSizeFull();
    setPadding(false);
    setSpacing(false);

    initGrid();

    add(ViewToolbarFactory.createGridToolbar(getGridObjectName()));
    add(grid);
  }

  protected void initGrid() {
    grid = new Grid<>();

    final var columns = getColumns();
    columns.forEach((valueProvider, header) -> addColumn(header, valueProvider));

    final var items = fetchItems();
    grid.setItems(items);
    grid.addItemDoubleClickListener(this::onGridItemDoubleClick);

    setDefaultSorting();
  }

  private void onGridItemDoubleClick(@Nonnull final ItemDoubleClickEvent<T> event) {
    final var url = Routes.getDetailURL(this.getClass());
    final var redirectURL = Routes.getURLWithId(url, event.getItem().getId());
    UI.getCurrent().navigate(redirectURL);
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

  protected void setDefaultSorting() {
    // Default implementation does nothing - override in subclasses if needed
  }

  @Nonnull
  protected abstract List<T> fetchItems();

  @Nonnull
  protected abstract String getGridObjectName();

  @Nonnull
  protected abstract Map<ValueProvider<T, Object>, String> getColumns();
}
