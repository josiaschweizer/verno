package ch.verno.ui.base.pages.grid;

import ch.verno.ui.base.components.grid.GridActionRoles;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.ValueProvider;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record ComponentGridColumn<T>(
        @Nonnull String key,
        @Nonnull ValueProvider<T, Component> component,
        @Nonnull String header,
        boolean sortable,
        @Nullable GridActionRoles... actionRoles
) {
}
