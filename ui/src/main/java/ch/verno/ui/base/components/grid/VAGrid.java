package ch.verno.ui.base.components.grid;

import ch.verno.ui.base.components.contextmenu.ActionDef;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.DataProvider;
import jakarta.annotation.Nonnull;

import java.util.Collection;
import java.util.List;

public class VAGrid<T> extends Grid<T> {

  public static final int DEFAULT_PAGE_SIZE = 50;

  public VAGrid() {
    this(DEFAULT_PAGE_SIZE);
  }

  public VAGrid(final int pageSize) {
    super(pageSize);
  }

  public VAGrid(final Class<T> beanType) {
    this(beanType, true);
  }

  public VAGrid(final Class<T> beanType, final boolean autoCreateColumns) {
    this();
    configureBeanType(beanType, autoCreateColumns);
  }

  public VAGrid(final DataProvider<T, Void> dataProvider) {
    this();
    setItems(dataProvider);
  }

  public VAGrid(final Collection<T> items) {
    this();
    setItems(items);
  }
}