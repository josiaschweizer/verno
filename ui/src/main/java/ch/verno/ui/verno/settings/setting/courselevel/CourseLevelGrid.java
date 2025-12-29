package ch.verno.ui.verno.settings.setting.courselevel;

import ch.verno.common.db.dto.CourseLevelDto;
import ch.verno.server.service.CourseLevelService;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@UIScope
@SpringComponent
public class CourseLevelGrid extends VerticalLayout {

  @Nullable
  protected Grid<CourseLevelDto> grid;
  @Nonnull
  protected final Map<String, Grid.Column<CourseLevelDto>> columnsByKey;
  @Nonnull
  private CourseLevelService courseLevelService;

  public CourseLevelGrid(@Nonnull final CourseLevelService courseLevelService) {
    this.courseLevelService = courseLevelService;
    this.columnsByKey = new LinkedHashMap<>();
  }

  @Autowired
  public void setCourseLevelService(@Nonnull final CourseLevelService courseLevelService) {
    this.courseLevelService = courseLevelService;
  }

  protected void initUI() {
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
                         @Nonnull final ValueProvider<CourseLevelDto, Object> valueProvider) {
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

  protected void addItemDoubleClickListener(@Nonnull final ComponentEventListener<ItemDoubleClickEvent<CourseLevelDto>> listener) {
    if (grid != null) {
      grid.addItemDoubleClickListener(listener);
    }
  }

  @Nonnull
  protected List<CourseLevelDto> fetchItems() {
    return courseLevelService.getAllCourseLevels();
  }

  protected void setDefaultSorting() {
    Grid.Column<CourseLevelDto> column = columnsByKey.get("Sorting Order");

    if (column != null && grid != null) {
      grid.sort(GridSortOrder.asc(column).build());
    }
  }

  @Nonnull
  protected Map<ValueProvider<CourseLevelDto, Object>, String> getColumns() {
    final var columnsMap = new LinkedHashMap<ValueProvider<CourseLevelDto, Object>, String>();
    columnsMap.put(CourseLevelDto::displayName, "Name");
    columnsMap.put(CourseLevelDto::getCode, "Code");
    columnsMap.put(CourseLevelDto::getDescription, "Description");
    columnsMap.put(CourseLevelDto::getSortingOrder, "Sorting Order");
    return columnsMap;
  }

  protected void refresh() {
    if (grid == null) {
      return;
    }

    final var items = fetchItems();
    grid.setItems(items);

    setDefaultSorting();
  }
}
