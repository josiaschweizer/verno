package ch.verno.ui.base.components.colorpicker;

import ch.verno.common.ui.base.components.colorpicker.Colors;
import org.vaadin.addons.tatu.ColorPicker;

import java.util.Arrays;
import java.util.List;


public class ColorPresets {

  public static List<ColorPicker.ColorPreset> getDefaultColorPresets() {
    return Arrays.asList(
            new ColorPicker.ColorPreset(Colors.PRIMARY_COLOR, "Primary"),
            new ColorPicker.ColorPreset(Colors.BLACK, "Black"),
            new ColorPicker.ColorPreset(Colors.WHITE, "White"),
            new ColorPicker.ColorPreset(Colors.RED, "Red"),
            new ColorPicker.ColorPreset(Colors.GREEN, "Green"),
            new ColorPicker.ColorPreset(Colors.YELLOW, "Yellow"),
            new ColorPicker.ColorPreset(Colors.CYAN, "Cyan"),
            new ColorPicker.ColorPreset(Colors.MAGENTA, "Magenta"),
            new ColorPicker.ColorPreset(Colors.GRAY, "Gray"),
            new ColorPicker.ColorPreset(Colors.ORANGE, "Orange"),
            new ColorPicker.ColorPreset(Colors.PURPLE, "Purple"),
            new ColorPicker.ColorPreset(Colors.PINK, "Pink")
    );
  }

}
