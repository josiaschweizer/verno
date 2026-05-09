package ch.verno.ui.base.components.colorpicker;

import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import org.vaadin.addons.tatu.ColorPicker;

import javax.annotation.Nullable;

public class VAColorPicker extends ColorPicker {

  public VAColorPicker(@Nonnull final String label) {
    super();
    setLabel(label);
    setNoClear(true);
  }

  @Override
  public void setValue(@Nullable final String value) {
    // if value is empty or does not start with a # the colorpicker throws an exception -> so we set it to null in this case
    if (value == null || value.isBlank() || !value.startsWith(Publ.HASH)) {
      super.setValue(null);
    } else {
      super.setValue(value);
    }
  }
}
