package ch.verno.ui.verno.settings.setting.courselevel;

import ch.verno.common.db.dto.table.CourseLevelDto;
import ch.verno.common.db.service.ICourseLevelService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.ui.base.settings.grid.BaseSettingGrid;
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
public class CourseLevelGrid extends BaseSettingGrid<CourseLevelDto> {

  @Nonnull
  private ICourseLevelService courseLevelService;

  public CourseLevelGrid(@Nonnull final GlobalInterface globalInterface) {
    this.courseLevelService = globalInterface.getService(ICourseLevelService.class);
  }

  @Autowired
  public void setCourseLevelService(@Nonnull final ICourseLevelService courseLevelService) {
    this.courseLevelService = courseLevelService;
  }

  @Nonnull
  @Override
  protected List<CourseLevelDto> fetchItems() {
    return courseLevelService.getAllCourseLevels();
  }

  @Nonnull
  @Override
  protected Map<ValueProvider<CourseLevelDto, Object>, String> getColumns() {
    final var columnsMap = new LinkedHashMap<ValueProvider<CourseLevelDto, Object>, String>();
    columnsMap.put(CourseLevelDto::displayName, getTranslation("setting.name"));
    columnsMap.put(CourseLevelDto::getCode, getTranslation("setting.code"));
    columnsMap.put(CourseLevelDto::getDescription, getTranslation("setting.description"));
    columnsMap.put(CourseLevelDto::getSortingOrder, getTranslation("setting.sorting.order"));
    return columnsMap;
  }

  @Nullable
  @Override
  protected String getDefaultSortColumnKey() {
    return getTranslation("setting.sorting.order");
  }
}