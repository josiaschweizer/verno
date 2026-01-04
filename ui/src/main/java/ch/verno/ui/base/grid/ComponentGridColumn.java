package ch.verno.ui.base.grid;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.ValueProvider;
import jakarta.annotation.Nonnull;

public record ComponentGridColumn<T>(@Nonnull String key,
                                     @Nonnull ValueProvider<T, Component> component,
                                     @Nonnull String header,
                                     boolean sortable) {
}
