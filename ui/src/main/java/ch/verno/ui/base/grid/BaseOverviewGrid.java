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

import java.util.List;
import java.util.Map;

public abstract class BaseOverviewGrid<T extends BaseDto> extends VerticalLayout {

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

  private void onGridItemDoubleClick(@Nonnull final ItemDoubleClickEvent<T> event) {
    final var url = Routes.getDetailURL(this.getClass());
    final var redirectURL = Routes.getURLWithId(url, event.getItem().getId());
    UI.getCurrent().navigate(redirectURL);
  }

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
