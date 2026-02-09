package ch.verno.ui.base.pages.grid;

import com.vaadin.flow.function.ValueProvider;
import jakarta.annotation.Nonnull;

public record ObjectGridColumn<T>(
        @Nonnull String key, // has to be named the same way as the property on the DTO
        @Nonnull ValueProvider<T, Object> valueProvider,
        @Nonnull String header,
        boolean sortable
) {
}
