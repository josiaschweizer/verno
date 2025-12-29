package ch.verno.ui.verno.settings;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.ui.base.settings.fields.BaseBooleanSetting;
import ch.verno.ui.base.settings.fields.BaseQuantitySetting;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class SettingEntryFactory<T extends BaseDto> {

  @Nonnull
  public BaseQuantitySetting<T> createQuantitySetting(@Nonnull final String label,
                                                      @Nonnull final Optional<String> tooltip,
                                                      @Nonnull final Binder<T> binder,
                                                      @Nonnull final ValueProvider<T, Integer> getter,
                                                      @Nonnull final Setter<T, Integer> setter) {
    final var quantitySetting = new BaseQuantitySetting<T>(label, binder, getter, setter);
    tooltip.ifPresent(quantitySetting::setTooltipText);
    return quantitySetting;
  }

  @Nonnull
  public BaseBooleanSetting<T> createBooleanSetting(@Nonnull final String label,
                                                    @Nonnull final Optional<String> tooltip,
                                                    @Nonnull final Binder<T> binder,
                                                    @Nonnull final ValueProvider<T, Boolean> getter,
                                                    @Nonnull final Setter<T, Boolean> setter) {
    final var booleanSetting = new BaseBooleanSetting<T>(label, binder, getter, setter);
    tooltip.ifPresent(booleanSetting::setTooltipText);
    return booleanSetting;
  }

}
