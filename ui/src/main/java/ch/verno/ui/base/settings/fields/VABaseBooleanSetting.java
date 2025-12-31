package ch.verno.ui.base.settings.fields;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import jakarta.annotation.Nonnull;

public class VABaseBooleanSetting<T> extends VABaseSettingRow {

  public VABaseBooleanSetting(@Nonnull final String title,
                              @Nonnull final Binder<T> binder,
                              @Nonnull final ValueProvider<T, Boolean> getter,
                              @Nonnull final Setter<T, Boolean> setter) {
    super(title, new Checkbox());
    final var field = (Checkbox) getComponentAt(1);

    binder.forField(field)
            .bind(getter, setter);
  }
}