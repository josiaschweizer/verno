package ch.verno.ui.verno.settings;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.ui.base.settings.fields.BaseQuantitySetting;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import jakarta.annotation.Nonnull;

public class SettingEntryFactory<T extends BaseDto> {

  @Nonnull
  public BaseQuantitySetting<T> createQuantitySetting(@Nonnull final String label,
                                                      @Nonnull final Binder<T> binder,
                                                      @Nonnull final ValueProvider<T, Integer> getter,
                                                      @Nonnull final Setter<T, Integer> setter) {
    return new BaseQuantitySetting<>(label, binder, getter, setter);
  }

}
