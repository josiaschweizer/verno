package ch.verno.ui.base.settings;

import ch.verno.ui.base.components.toolbar.ViewToolbarFactory;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

import java.util.List;

public abstract class VABaseSettingsPage extends VerticalLayout {

  protected void initUI() {
    setSizeFull();

    add(ViewToolbarFactory.createSimpleToolbar(getSettingsPageName()));

    final var settingsRow = new HorizontalLayout();
    settingsRow.setWidthFull();
    settingsRow.setPadding(false);
    settingsRow.setSpacing(true);
    settingsRow.setWrap(true);

    for (final var setting : createSettings()) {
      setting.getStyle()
              .set("min-width", "320px");

      settingsRow.add(setting);
    }

    add(settingsRow);
  }

  @Nonnull
  protected abstract List<VABaseSetting> createSettings();

  @Nonnull
  protected abstract String getSettingsPageName();
}