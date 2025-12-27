package ch.verno.ui.base.settings;

import ch.verno.ui.base.components.toolbar.ViewToolbarFactory;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

import java.util.List;

public abstract class VABaseSettingsPage extends VerticalLayout {

  public VABaseSettingsPage() {
  }

  protected void initUI() {
    setSizeFull();
//    setPadding(false);
//    setSpacing(false);

    add(ViewToolbarFactory.createSimpleToolbar(getSettingsPageName()));

    final var settings = createSettings();
    for (final var setting : settings) {
      add(setting);
    }
  }

  @Nonnull
  protected abstract List<VABaseSetting> createSettings();

  @Nonnull
  protected abstract String getSettingsPageName();
}
