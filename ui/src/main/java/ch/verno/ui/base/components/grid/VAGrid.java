package ch.verno.ui.base.components.grid;

import ch.verno.ui.base.components.contextmenu.ActionDef;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.data.provider.DataProvider;
import jakarta.annotation.Nonnull;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class VAGrid<T> extends Grid<T> {

  public static final int DEFAULT_PAGE_SIZE = 50;

  public VAGrid() {
    this(DEFAULT_PAGE_SIZE);
  }

  public VAGrid(final int pageSize) {
    super(pageSize);
  }

  public VAGrid(@Nonnull final Class<T> beanType) {
    this(beanType, true);
  }

  public VAGrid(@Nonnull final Class<T> beanType,
                final boolean autoCreateColumns) {
    this();
    configureBeanType(beanType, autoCreateColumns);
  }

  public VAGrid(@Nonnull final DataProvider<T, Void> dataProvider) {
    this();
    setItems(dataProvider);
  }

  public VAGrid(@Nonnull final Collection<T> items) {
    this();
    setItems(items);
  }

  @Nonnull
  public GridContextMenu<T> addContextMenu(@Nonnull final Function<T, List<ActionDef>> actionsFunction) {
    final var gridContextMenu = addContextMenu();
    gridContextMenu.setDynamicContentHandler(dto -> {
      gridContextMenu.removeAll();
      if (dto == null) {
        return false;
      }

      final var actions = actionsFunction.apply(dto);
      if (actions.isEmpty()) {
        return false;
      }

      for (final var action : actions) {
        final var item = gridContextMenu.addItem(action.getComponent(), e -> action.getRunnable().run());
        item.setEnabled(action.isEnabled());
      }

      return true;
    });
    return gridContextMenu;
  }


}