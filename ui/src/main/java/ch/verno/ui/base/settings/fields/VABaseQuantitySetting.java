package ch.verno.ui.base.settings.fields;

import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import jakarta.annotation.Nonnull;

public class VABaseQuantitySetting<T> extends VABaseSettingRow {

  public VABaseQuantitySetting(
          @Nonnull final String title,
          @Nonnull final Binder<T> binder,
          @Nonnull final ValueProvider<T, Integer> getter,
          @Nonnull final Setter<T, Integer> setter) {
    super(title, createField());

    final var field = (NumberField) getComponentAt(1);

    binder.forField(field)
            .withConverter(
                    v -> v == null ? null : v.intValue(),
                    i -> i == null ? null : i.doubleValue(),
                    getTranslation("base.invalid.number")
            )
            .bind(getter, setter);
  }

  @Nonnull
  private static NumberField createField() {
    final var field = new NumberField();
    field.setWidth("140px");
    field.setMin(0);
    field.setStep(1);
    field.setStepButtonsVisible(true);
    return field;
  }
}