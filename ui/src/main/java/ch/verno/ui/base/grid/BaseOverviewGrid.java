package ch.verno.ui.base.grid;

import ch.verno.ui.base.components.toolbar.ViewToolbarFactory;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Map;

public abstract class BaseOverviewGrid<T> extends VerticalLayout {

  @Nullable
  private Grid<T> grid;

  public BaseOverviewGrid() {
    // empty constructor
  }

  @Override
  protected void onAttach(final AttachEvent attachEvent) {
    initGrid();
  }

  private void initGrid() {
    grid = new Grid<>();

    final var columns = getColumns();
    columns.forEach(this::addColumn);

    final var items = fetchItems();
    grid.setItems(items);
    grid.addItemDoubleClickListener(this::onGridItemDoubleClick);

    setSizeFull();
    setPadding(false);
    setSpacing(false);

    add(ViewToolbarFactory.createGridToolbar(getGridObjectName()));
    add(grid);
  }

  protected abstract void onGridItemDoubleClick(@Nonnull final ItemDoubleClickEvent<T> event);

  private void addColumn(@Nonnull final ValueProvider<T, Object> valueProvider,
                         @Nonnull final String header) {
    if (grid == null) {
      throw new IllegalStateException("Grid has not been initialized. Call initGrid() first.");
    }

    grid.addColumn(valueProvider)
        .setHeader(header)
        .setResizable(true)
        .setAutoWidth(true);
  }

  @Nonnull
  protected abstract List<T> fetchItems();

  @Nonnull
  protected abstract String getGridObjectName();

  @Nonnull
  protected abstract Map<ValueProvider<T, Object>, String> getColumns();
}
