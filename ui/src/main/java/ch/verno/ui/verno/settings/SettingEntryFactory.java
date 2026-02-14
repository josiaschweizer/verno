package ch.verno.ui.verno.settings;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.ui.base.settings.fields.VABaseBooleanSetting;
import ch.verno.ui.base.settings.fields.VABaseComboBoxSetting;
import ch.verno.ui.base.settings.fields.VABaseQuantitySetting;
import ch.verno.ui.base.settings.fields.VABaseToggleSetting;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import jakarta.annotation.Nonnull;

import java.util.Collection;
import java.util.Optional;

public class SettingEntryFactory<T extends BaseDto> {

  @Nonnull
  public VABaseQuantitySetting<T> createQuantitySetting(@Nonnull final String label,
                                                        @Nonnull final Optional<String> tooltip,
                                                        @Nonnull final Binder<T> binder,
                                                        @Nonnull final ValueProvider<T, Integer> getter,
                                                        @Nonnull final Setter<T, Integer> setter) {
    final var quantitySetting = new VABaseQuantitySetting<T>(label, binder, getter, setter);
    tooltip.ifPresent(quantitySetting::setTooltipText);
    return quantitySetting;
  }

  @Nonnull
  public VABaseBooleanSetting<T> createBooleanSetting(@Nonnull final String label,
                                                      @Nonnull final Optional<String> tooltip,
                                                      @Nonnull final Binder<T> binder,
                                                      @Nonnull final ValueProvider<T, Boolean> getter,
                                                      @Nonnull final Setter<T, Boolean> setter) {
    final var booleanSetting = new VABaseBooleanSetting<T>(label, binder, getter, setter);
    tooltip.ifPresent(booleanSetting::setTooltipText);
    return booleanSetting;
  }

  @Nonnull
  public VABaseToggleSetting<T> createToggleSetting(@Nonnull final String label,
                                                    @Nonnull final String leftLabel,
                                                    @Nonnull final String rightLabel,
                                                    @Nonnull final Optional<String> tooltip,
                                                    @Nonnull final Binder<T> binder,
                                                    @Nonnull final ValueProvider<T, Boolean> getter,
                                                    @Nonnull final Setter<T, Boolean> setter) {
    final var toggleSetting = new VABaseToggleSetting<T>(label, leftLabel, rightLabel, binder, getter, setter);
    tooltip.ifPresent(toggleSetting::setTooltipText);
    return toggleSetting;
  }


  @Nonnull
  public <V> VABaseComboBoxSetting<V> createComboBoxSetting(@Nonnull final String label,
                                                            @Nonnull final Optional<String> tooltip,
                                                            @Nonnull final Binder<T> binder,
                                                            @Nonnull final ValueProvider<T, V> getter,
                                                            @Nonnull final Setter<T, V> setter,
                                                            @Nonnull final Collection<V> items,
                                                            @Nonnull final ItemLabelGenerator<V> itemLabelGenerator) {
    final var setting = new VABaseComboBoxSetting<V>(label);

    setting.getComboBox().setItems(items);
    setting.getComboBox().setItemLabelGenerator(itemLabelGenerator);

    binder.forField(setting.getComboBox()).bind(getter, setter);

    tooltip.ifPresent(setting::setTooltipText);
    return setting;
  }

}
