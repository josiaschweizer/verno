package ch.verno.ui.verno.settings.panels.courselevel;

import ch.verno.contract.dto.table.course.CourseLevelDto;
import ch.verno.lib.Lazy;
import ch.verno.rpc.client.course.CourseLevelClient;
import ch.verno.ui.lib.settings.grid.BaseSettingGrid;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@UIScope
@SpringComponent
public class CourseLevelGrid extends BaseSettingGrid<CourseLevelDto> {

  @Nonnull private final Lazy<CourseLevelClient> courseLevelClient;

  @Inject
  public CourseLevelGrid(@Nonnull final Injector injector) {
    this.courseLevelClient = Lazy.of(() -> injector.getInstance(CourseLevelClient.class));
  }
//
//  @Autowired
//  public void setCourseLevelService(@Nonnull final CourseLevelClient courseLevelClient) {
//    this.courseLevelClient = Lazy.of(() -> courseLevelClient);
//  }

  @Nonnull
  @Override
  protected List<CourseLevelDto> fetchItems() {
    return courseLevelClient.get().getAllCourseLevels();
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