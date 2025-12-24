package ch.verno.ui.base.components.toolbar;

import com.vaadin.flow.component.button.Button;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record ViewToolbarResult(
    @Nonnull ViewToolbar toolbar,
    @Nullable Button createButton,
    @Nullable Button editButton
) {
}
