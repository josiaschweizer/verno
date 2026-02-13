package ch.verno.ui.base.components.colorpicker;

import jakarta.annotation.Nonnull;
import org.vaadin.addons.tatu.ColorPicker;

public class VAColorPicker extends ColorPicker {

  public VAColorPicker(@Nonnull final String label) {
    super();
    setLabel(label);
    setNoClear(true);
  }
  
}
