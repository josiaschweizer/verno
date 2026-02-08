package ch.verno.ui.lib.icon;

import com.vaadin.flow.component.Component;
import jakarta.annotation.Nonnull;

public final class CustomIconUtil {

  private CustomIconUtil() {
  }

  @Nonnull
  public static Component create(@Nonnull final CustomIcons icon) {
    return create(icon, "var(--lumo-icon-size-m)");
  }

  @Nonnull
  public static Component create(@Nonnull final CustomIcons icon, @Nonnull final String size) {
    return new CustomIcon(icon.getPath(), size);
  }
}