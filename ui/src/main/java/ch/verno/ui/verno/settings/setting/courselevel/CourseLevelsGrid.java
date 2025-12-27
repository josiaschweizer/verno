package ch.verno.ui.verno.settings.setting.courselevel;

import ch.verno.common.db.dto.CourseLevelDto;
import ch.verno.common.util.VernoConstants;
import ch.verno.server.service.CourseLevelService;
import ch.verno.ui.base.grid.BaseOverviewGrid;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.function.ValueProvider;
import jakarta.annotation.Nonnull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CourseLevelsGrid extends BaseOverviewGrid<CourseLevelDto> {

  @Nonnull
  private final CourseLevelService courseLevelService;

  public CourseLevelsGrid(@Nonnull final CourseLevelService courseLevelService) {
    this.courseLevelService = courseLevelService;
  }

  @Nonnull
  @Override
  protected List<CourseLevelDto> fetchItems() {
    return courseLevelService.getAllCourseLevels();
  }

  @Nonnull
  @Override
  protected String getGridObjectName() {
    return VernoConstants.COURSE_LEVEL;
  }

  @Override
  protected void setDefaultSorting() {
    Grid.Column<CourseLevelDto> column = columnsByKey.get("Sorting Order");

    if (column != null && grid != null) {
      grid.sort(GridSortOrder.asc(column).build());
    }
  }

  @Nonnull
  @Override
  protected Map<ValueProvider<CourseLevelDto, Object>, String> getColumns() {
    final var columnsMap = new LinkedHashMap<ValueProvider<CourseLevelDto, Object>, String>();
    columnsMap.put(CourseLevelDto::displayName, "Name");
    columnsMap.put(CourseLevelDto::getCode, "Code");
    columnsMap.put(CourseLevelDto::getDescription, "Description");
    columnsMap.put(CourseLevelDto::getSortingOrder, "Sorting Order");
    return columnsMap;
  }
}
