package ch.verno.ui.base.pages.grid;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.ValueProvider;
import jakarta.annotation.Nonnull;

public class GridColumnHelper {

  @Nonnull
  public static <T> ObjectGridColumn<T> objectCol(@Nonnull final String key,
                                                  @Nonnull final ValueProvider<T, Object> valueProvider,
                                                  @Nonnull final String header) {
    return new ObjectGridColumn<>(key, valueProvider, header, true);
  }

  @Nonnull
  public static <T> ObjectGridColumn<T> objectCol(@Nonnull final String key,
                                                  @Nonnull final ValueProvider<T, Object> valueProvider,
                                                  @Nonnull final String header,
                                                  final boolean sortable) {
    return new ObjectGridColumn<>(key, valueProvider, header, sortable);
  }

  @Nonnull
  public static <T> ComponentGridColumn<T> componentCol(@Nonnull final String key,
                                                        @Nonnull final ValueProvider<T, Component> component,
                                                        @Nonnull final String header) {
    return new ComponentGridColumn<>(key, component, header, false);
  }

  @Nonnull
  public static <T> ComponentGridColumn<T> componentCol(@Nonnull final String key,
                                                        @Nonnull final ValueProvider<T, Component> component,
                                                        @Nonnull final String header,
                                                        final boolean sortable) {
    return new ComponentGridColumn<>(key, component, header, sortable);
  }

}
