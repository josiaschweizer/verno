package ch.verno.ui.base.settings.fields;

import ch.verno.ui.base.components.toggle.VAToggleField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import jakarta.annotation.Nonnull;

public class VABaseToggleSetting<T> extends VABaseSettingRow {

  public VABaseToggleSetting(@Nonnull final String title,
                           @Nonnull final String leftLabel,
                           @Nonnull final String rightLabel,
                           @Nonnull final Binder<T> binder,
                           @Nonnull final ValueProvider<T, Boolean> getter,
                           @Nonnull final Setter<T, Boolean> setter) {
    super(title, new VAToggleField(leftLabel, rightLabel));
    final var field = (VAToggleField) getComponentAt(1);

    binder.forField(field)
            .bind(getter, setter);
  }
}