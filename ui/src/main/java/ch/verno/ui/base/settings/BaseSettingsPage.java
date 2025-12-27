package ch.verno.ui.base.settings;

import ch.verno.ui.base.components.toolbar.ViewToolbarFactory;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

public abstract class BaseSettingsPage extends VerticalLayout {

  public BaseSettingsPage() {
    setSizeFull();
    setPadding(false);
    setSpacing(false);

    add(ViewToolbarFactory.createSimpleToolbar(getSettingsPageName()));
  }

  @Nonnull
  protected abstract String getSettingsPageName();
}
