package ch.verno.ui.base.shortcut;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record VAShortcut(@Nonnull Key key,
                         @Nullable KeyModifier... keyModifier) {

}
