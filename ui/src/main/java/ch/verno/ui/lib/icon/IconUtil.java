package ch.verno.ui.lib.icon;

import ch.verno.publ.VernoUtility;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import jakarta.annotation.Nonnull;

public final class IconUtil {

  private IconUtil() {
  }

  @Nonnull
  public static VAIcon create(@Nonnull final CustomIcons icon) {
    return create(icon, VernoUtility.LUMO_ICON_SIZE_M);
  }

  @Nonnull
  public static VAIcon create(@Nonnull final CustomIcons icon, @Nonnull final String size) {
    return new VAIcon(icon.getPath(), size);
  }

  @Nonnull
  public static VAIcon create(@Nonnull final VaadinIcon icon) {
    return create(icon, VernoUtility.LUMO_ICON_SIZE_M);
  }

  @Nonnull
  public static VAIcon create(@Nonnull final VaadinIcon icon, @Nonnull final String size) {
    return new VAIcon(icon, size);
  }
}