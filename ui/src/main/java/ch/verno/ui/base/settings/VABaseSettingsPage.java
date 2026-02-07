package ch.verno.ui.base.settings;

import ch.verno.ui.base.components.toolbar.ViewToolbarFactory;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

import java.util.List;

public abstract class VABaseSettingsPage extends VerticalLayout {

  protected void initUI() {
    setPadding(false);
    setSpacing(false);
    setSizeFull();

    add(ViewToolbarFactory.createSimpleToolbar(getSettingsPageName()));

    final var settingsRow = new HorizontalLayout();
    settingsRow.setWidthFull();
    settingsRow.setPadding(true);
    settingsRow.setSpacing(true);
    settingsRow.setWrap(true);

    for (final var setting : getSettings()) {
      // Make each setting a flexible panel: allow it to grow and shrink but keep a sensible
      // minimum and maximum width so panels themselves resize when the viewport changes.
      setting.getStyle()
              .set("flex", "1 1 320px");
      setting.getStyle()
              .set("min-width", "320px");
      setting.getStyle()
              .set("max-width", "900px");

      settingsRow.add(setting);
    }

    add(settingsRow);
  }

  @Nonnull
  protected abstract List<VABaseSetting<?>> getSettings();

  @Nonnull
  protected abstract String getSettingsPageName();
}